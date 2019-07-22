package org.fmod.youyaoqi2.fragment

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.book_detail.*
import kotlinx.android.synthetic.main.book_detail.view.*
import kotlinx.android.synthetic.main.book_details_chapter_item.view.*
import org.fmod.youyaoqi2.R
import org.fmod.youyaoqi2.Util
import org.fmod.youyaoqi2.bean.details.BookDetails
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

class BookDetailsFragment: BaseFragment() {
    private var isSubscibe = false
    private val roundedTransformation = RoundedTransformationBuilder()
        .cornerRadiusDp(5f)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.book_detail,container,false).apply {
            subscribe_efab.setOnClickListener {
                animateFabIcon()
                isSubscibe = !isSubscibe
            }
        }
    }

    override fun onStart() {
        EventBus.getDefault().register(this)
        super.onStart()
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    private fun animateFabIcon(){
        subscribe_efab.apply {
            shrink()
            if(isSubscibe){
                icon = resources.getDrawable(R.drawable.subscribe_cancel,null)
                text = "订阅"
            }
            else{
                icon = resources.getDrawable(R.drawable.subscribe_ok,null)
                text = "已订"
            }
            (icon as AnimatedVectorDrawable).start()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public fun getBookDetails(bookDetails: BookDetails){
        // 再次onStart时后EventBus被注册，会再尝试拿出stickyEvent
        // remove以相当于consume事件
        EventBus.getDefault().removeStickyEvent(bookDetails)
        //加载数据到视图
        book_detail_appbar.setExpanded(true,false)
        book_detail_scroll.scrollTo(0,0)
        bookDetails.comic.run {
            book_detail_name.text = name
            book_detail_description.text = description
            book_detail_author.text = author.name
            Picasso.get()
                .load(cover)
                .transform(roundedTransformation)
                .into(book_detail_cover)
            //加载背景
            //色值必须先转Long再Int
            book_detail_background.foreground = ColorDrawable("AA$wideColor".toLong(16).toInt())//wideColor.toInt(16))
            if(wideCover.isEmpty()){
                Picasso.get().load(cover).into(book_detail_background)
                //book_detail_background.foreground = ColorDrawable("AAFFFFFF".toLong(16).toInt())
            }else{
                Picasso.get().load(wideCover).into(book_detail_background)

            }

        }

        //展示部分列表，采用addView
        var info: String
        var date: Date
        var v : View
        var viewIndex = 0
        val format = SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)
        book_detail_small_list.removeAllViews()
        for (i in bookDetails.chapterList){
            log(i.smallPlaceCover)
            if(viewIndex == 5){
                break
            }
            v = layoutInflater.inflate(R.layout.book_details_chapter_item,book_detail_small_list,false).apply {
                Picasso.get()
                    .load(i.smallPlaceCover)
                    .transform(roundedTransformation)
                    .into(chapter_item_cover)
                chapter_item_name.text = i.name
                date = Date(i.publishTime*1000)
                info = "第${i.index}话   ${format.format(date)}"
                chapter_item_info.text = info
            }
            book_detail_small_list.addView(v,viewIndex++)
        }
        val textView = TextView(activity).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            val paddingV = Util.dp2px(15)
            setPadding(0,paddingV,0,paddingV)
            text = resources.getString(R.string.expand_more)
            setTextColor(resources.getColor(R.color.colorTheme,null))
            gravity = Gravity.CENTER
            setOnClickListener {
                //展开章节列表

            }
        }
        book_detail_small_list.addView(textView,viewIndex)
    }

}