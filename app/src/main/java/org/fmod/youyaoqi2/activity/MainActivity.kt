package org.fmod.youyaoqi2.activity

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.book_detail.*
import kotlinx.android.synthetic.main.suggestion_item.*
import org.fmod.youyaoqi2.R
import org.fmod.youyaoqi2.adapter.MyFragmentPagerAdapter
import org.fmod.youyaoqi2.bean.HomeBean
import org.fmod.youyaoqi2.bean.details.BookDetails
import org.fmod.youyaoqi2.bean.search.SearchResult
import org.fmod.youyaoqi2.bean.search.SearchSuggestion
import org.fmod.youyaoqi2.fragment.BookDetailsFragment
import org.fmod.youyaoqi2.fragment.Fragment4
import org.fmod.youyaoqi2.fragment.SearchFragment
import org.fmod.youyaoqi2.manager.DatabaseManager
import org.fmod.youyaoqi2.manager.JsonManager
import org.fmod.youyaoqi2.manager.NetworkManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.properties.Delegates

class MainActivity : BaseActivity() {
    private var menuItem: MenuItem? = null
    private lateinit var fragmentPagerAdapter: MyFragmentPagerAdapter
    private val networkManager = NetworkManager()

    private lateinit var searchFragment : SearchFragment
    private lateinit var detailsFragment: BookDetailsFragment

    private val databaseManager = DatabaseManager()

    private lateinit var homeBean: HomeBean
    private lateinit var searchView: SearchView
    private var searchMenuItem: MenuItem? = null

    private var searchResultShow: Boolean by Delegates.observable(false){
        _,old,new->
        if(old && !new){
            //退出搜索结果状态，恢复标题
            main_toolbar.title = getString(R.string.main_title)
        }
    }
    private var bookDetailsShow = false
    private lateinit var searchContent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this)

        setListener()
        setNetworkListener()

        initFace()

    }

    private fun setNetworkListener(){
        networkManager.run {
            //展示主页随机漫画列表
            homeCallBack = object :NetworkManager.HomeCallback{
                override fun onComplete(json: String?) {
                    homeBean = JsonManager.parseHomeJson(json as String)
                    loadDataToFace()
                }
            }

            //展示搜索结果
            searchResultCallback = object :NetworkManager.SearchCallback{
                override fun onComplete(json: String?) {
                    EventBus.getDefault().post(
                        JsonManager.parseSearchResult(json as String).apply {
                            toSearch = searchContent
                        })
                }
            }

            //展示漫画详情
            bookDetailCallback = object : NetworkManager.DetailCallback{
                override fun onComplete(json: String?) {
                    //log("details: ${}")
                    showDetailsFragment(JsonManager.parseBookDetails(json as String))
                }
            }

            getHomeJson()
        }
    }

    private fun loadDataToFace(){
        runOnUiThread{
            searchView.run {
                queryHint = homeBean.defaultSearch
            }

        }
    }

    private fun setListener(){
        bottom_navigation.setOnNavigationItemSelectedListener {
            home_viewpager.setCurrentItem(
                when(it.itemId){
                    R.id.update -> {
                        0
                    }
                    R.id.bookshelf ->{
                        2
                    }
                    R.id.mine ->{
                        3
                    }
                    else -> 1 //discovery
                }, false
            )
            true
        }

        home_viewpager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                if(menuItem == null){
                    bottom_navigation.menu.getItem(0).isChecked = false
                }else{
                    menuItem?.isChecked = false
                }
                menuItem = bottom_navigation.menu.getItem(p0)
                menuItem?.isChecked = true
            }
        })
    }

    private fun initFace() {
        setSupportActionBar(main_toolbar)
        transparentStatusBar()
        //paddingTop一个状态栏高度
        //同时Toolbar的高度加一个状态栏高度
        val params = main_toolbar.layoutParams as ConstraintLayout.LayoutParams
        params.height += getStatusBarHeight()
        main_toolbar.setPadding(0,getStatusBarHeight(),0,0)


        fragmentPagerAdapter = MyFragmentPagerAdapter(supportFragmentManager)
        home_viewpager.adapter = fragmentPagerAdapter
        home_viewpager.currentItem = 1

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        //searchView动作监听
        searchMenuItem = (menu?.findItem(R.id.action_search))?.apply {
            setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    //关闭
                    if(!searchResultShow) {
                        hideFragment(searchFragment)
                    }
                    return true
                }

                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    //打开
                    if(!searchResultShow) {
                        showSearchFragment()
                    }else{
                        EventBus.getDefault().post(databaseManager.getSearchHistory())
                        searchResultShow = false
                    }
                    return true
                }
            })
        }
        searchView = (searchMenuItem?.actionView as androidx.appcompat.widget.SearchView).apply {
            setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                override fun onQueryTextChange(newText: String?): Boolean {
                    //改变内容
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    //提交内容
                    if(!query.isNullOrBlank()){
                        val toSubmit = query.trim()
                        searchContent = toSubmit

                        submitQuery(query)
                        databaseManager.updateHistory(toSubmit)
                    }
                    return false
                }
            })
        }
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun getQueryFromSuggestion(query: SearchSuggestion){
        searchContent = query.text
        submitQuery(query.text)
    }

    // 打开searchFragment必定携带
    // 包括historySuggestion
    private fun showSearchFragment(){
        if(!this::searchFragment.isInitialized){
            EventBus.getDefault().postSticky(databaseManager.getSearchHistory())
            searchFragment = SearchFragment().apply {
                onClickOutsideCloseListener = object : SearchFragment.OnClickOutsideCloseListener{
                    override fun onClick() {
                        hideFragment(searchFragment)
                        searchMenuItem?.collapseActionView()
                    }
                }

                onOpenBookDetails = object : SearchFragment.OnOpenBookDetails{
                    override fun onOpen(comicId: Long) {
                        networkManager.getBookDetailJson(comicId)
                    }
                }
            }
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container,searchFragment)
                .commit()
        }else{
            EventBus.getDefault().post(databaseManager.getSearchHistory())
            supportFragmentManager.beginTransaction()
                .show(searchFragment)
                .commit()
        }
    }

    // 打开detailsFragment必定携带数据
    // 包括details数据
    private fun showDetailsFragment(data: BookDetails){
        bookDetailsShow = true
        if(!this::detailsFragment.isInitialized){
            EventBus.getDefault().postSticky(data)
            detailsFragment = BookDetailsFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.main_container,detailsFragment)
                .commit()
        }else{
            EventBus.getDefault().post(data)
            supportFragmentManager.beginTransaction()
                .show(detailsFragment)
                .commit()
        }
    }

    private fun hideFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .hide(fragment)
            .commit()
    }

    private fun submitQuery(query: String){
        searchResultShow = true
        searchMenuItem?.collapseActionView()

        main_toolbar.title = "搜索:$query"
        networkManager.getSearchResultJson(query,1)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onBackPressed() {
        when{
            bookDetailsShow -> {
                //关闭漫画详情
                bookDetailsShow = false
                hideFragment(detailsFragment)
            }
            searchResultShow -> {
                //关闭搜索结果Fragment
                searchResultShow = false
                hideFragment(searchFragment)
            }

            else -> super.onBackPressed()
        }
    }

}
