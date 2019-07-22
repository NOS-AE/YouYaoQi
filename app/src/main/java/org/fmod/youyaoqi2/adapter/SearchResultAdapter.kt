package org.fmod.youyaoqi2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_item.view.*
import kotlinx.android.synthetic.main.search_result_hint_item.view.*
import kotlinx.android.synthetic.main.search_result_item.view.*
import kotlinx.android.synthetic.main.search_result_loadmore_item.view.*
import org.fmod.youyaoqi2.R
import org.fmod.youyaoqi2.ViewHolder
import org.fmod.youyaoqi2.bean.search.SearchResultComic
import kotlin.math.log

class SearchResultAdapter(
    var mList: ArrayList<SearchResultComic>,
    var comicNum: Int,
    var hasMore: Boolean): RecyclerView.Adapter<ViewHolder>() {
    companion object {
        //VIEW_TYPE
        const val VT_EMPTY = 0
        const val VT_RESULT = 1
        const val VT_RESULT_HINT = 2
        const val VT_LOAD_MORE = 3
    }

    var comicClickListener: OnComicClickListener? = null
    var loadMoreListener: OnLoadMoreListener? = null
    //Picasso & RoundedImage transformation
    private val roundedTransformation = RoundedTransformationBuilder()
        .cornerRadiusDp(5f)
        .build()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.run {
            when(holder.itemViewType){
                VT_RESULT_HINT -> {
                    val text = "共找到 $comicNum 本相关漫画"
                    search_result_hint.text = text
                }
                VT_RESULT -> {
                    val arrayPos = position - 1
                    search_result_container.setOnClickListener {
                        comicClickListener?.onClick(mList[arrayPos].comicId)
                    }

                    Picasso.get()
                        .load(mList[position-1].cover)
                        .transform(roundedTransformation)
                        .into(search_result_cover)

                    search_result_title.text = mList[arrayPos].name
                    search_result_author.text = mList[arrayPos].author
                }
                VT_LOAD_MORE -> {
                    if(hasMore) {
                        search_result_load_more.text = "正在加载更多…"
                        loadMoreListener?.load()
                    }
                    else{
                        search_result_load_more.text = "没有更多了哟~"
                    }
                }
                else -> {
                    //EMPTY
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            when(viewType){
                VT_EMPTY -> R.layout.search_result_empty_item
                VT_RESULT_HINT -> R.layout.search_result_hint_item
                VT_LOAD_MORE -> R.layout.search_result_loadmore_item
                else -> R.layout.search_result_item
            },
            parent,false
        )

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        var count = mList.size + 1
        if(mList.isNotEmpty())
            count++
        return count
    }

    override fun getItemViewType(position: Int): Int {
        return when{
            mList.isNullOrEmpty() -> VT_EMPTY
            position == 0 -> VT_RESULT_HINT
            position <= mList.size -> VT_RESULT
            else -> VT_LOAD_MORE
        }
    }

    interface OnComicClickListener{
        fun onClick(comicId: Long)
    }
    interface OnLoadMoreListener{
        fun load()
    }
}