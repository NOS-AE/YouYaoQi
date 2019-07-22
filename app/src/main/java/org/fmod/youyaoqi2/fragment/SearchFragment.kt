package org.fmod.youyaoqi2.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.search_fragment_layout.*
import kotlinx.android.synthetic.main.search_fragment_layout.view.*
import org.fmod.youyaoqi2.R
import org.fmod.youyaoqi2.adapter.SearchResultAdapter
import org.fmod.youyaoqi2.adapter.SuggestionAdapter
import org.fmod.youyaoqi2.bean.details.BookDetails
import org.fmod.youyaoqi2.bean.search.SearchResult
import org.fmod.youyaoqi2.bean.search.SearchResultComic
import org.fmod.youyaoqi2.bean.search.SearchSuggestion
import org.fmod.youyaoqi2.manager.DatabaseManager
import org.fmod.youyaoqi2.manager.JsonManager
import org.fmod.youyaoqi2.manager.NetworkManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SearchFragment: BaseFragment(){

    var onClickOutsideCloseListener: OnClickOutsideCloseListener? = null
    var onOpenBookDetails: OnOpenBookDetails? = null
    private var networkManager = NetworkManager()
    private lateinit var suggestionAdapter: SuggestionAdapter
    private lateinit var searchResultAdapter: SearchResultAdapter
    private var searchResultPage = 1

    private lateinit var currentResult: SearchResult

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        EventBus.getDefault().register(this)

        //获取更多搜索结果
        networkManager.searchResultCallback = object : NetworkManager.SearchCallback{
            override fun onComplete(json: String?) {
                loadMoreResult(JsonManager.parseSearchResult(json as String))
            }
        }

        return inflater.inflate(R.layout.search_fragment_layout, container, false).apply {
            this.search_list_container.setOnClickListener {
                onClickOutsideCloseListener?.onClick()
            }
            search_list.layoutManager = LinearLayoutManager(activity)
            search_list.adapter = suggestionAdapter
            /*val testId = View.generateViewId()
            search_list_container.run {
                addView(
                    TextView(activity).apply {
                        id = testId
                        layoutParams = ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT).apply {
                            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                            topMargin = 100
                        }
                        text = "hello world"
                    }
                )
                (getChildAt(0).layoutParams as ConstraintLayout.LayoutParams).topToTop = testId
            }*/

        }
    }

    private fun loadSuggestion(suggestion: ArrayList<SearchSuggestion>){
        if(this::suggestionAdapter.isInitialized){
            suggestionAdapter.mList = suggestion
            suggestionAdapter.notifyDataSetChanged()
            if(search_list.adapter !is SuggestionAdapter)
                search_list.adapter = suggestionAdapter
        }else{
            suggestionAdapter = SuggestionAdapter(suggestion).apply {
                suggestionClickListener = object : SuggestionAdapter.OnSuggestionClickListener{
                    override fun onClick(query: SearchSuggestion) {
                        EventBus.getDefault().post(query)
                        log("submit suggestion")
                    }
                }
                suggestionDeleteListener = object : SuggestionAdapter.OnSuggestionDeleteListener{
                    override fun onClick(item: SearchSuggestion) {
                        item.delete()
                    }
                }
            }
        }
    }
    private fun loadMoreResult(result: SearchResult){
        activity?.runOnUiThread {
            searchResultAdapter.mList.addAll(result.comics)
            searchResultAdapter.hasMore = result.hasMore
            searchResultAdapter.notifyDataSetChanged()
        }
    }

    private fun loadResult(result: SearchResult){
        currentResult = result
        searchResultPage = 1
        if(this::searchResultAdapter.isInitialized){
            result.run {
                searchResultAdapter.mList = comics
                searchResultAdapter.hasMore = hasMore
                searchResultAdapter.comicNum = comicNum
            }

            searchResultAdapter.notifyDataSetChanged()
        }else{
            searchResultAdapter = SearchResultAdapter(result.comics, result.comicNum, result.hasMore).apply {
                comicClickListener = object : SearchResultAdapter.OnComicClickListener{
                    override fun onClick(comicId: Long) {
                        onOpenBookDetails?.onOpen(comicId)
                    }
                }

                loadMoreListener = object : SearchResultAdapter.OnLoadMoreListener{
                    override fun load() {
                        networkManager.getSearchResultJson(currentResult.toSearch,++searchResultPage)
                    }
                }
            }
        }
        search_list.adapter = searchResultAdapter
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public fun getSearchSuggestion(suggestion: ArrayList<SearchSuggestion>){
        loadSuggestion(suggestion)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun getSearchResult(result: SearchResult){
        loadResult(result)
    }

    interface OnOpenBookDetails{
        fun onOpen(comicId: Long)
    }

    interface OnClickOutsideCloseListener{
        fun onClick()
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }
}