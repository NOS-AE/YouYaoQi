package org.fmod.youyaoqi2.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_item.view.*
import org.fmod.youyaoqi2.R
import org.fmod.youyaoqi2.ViewHolder
import org.fmod.youyaoqi2.bean.ModuleItem

class HomeListAdapter(
   var mList: List<ModuleItem>
): androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {

    private lateinit var mBookClickListener: OnBookClickListener

    fun setOnBookClickListener(listener: OnBookClickListener){
        mBookClickListener = listener
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemView.run {
            setOnClickListener {
                if(this@HomeListAdapter::mBookClickListener.isInitialized){
                    mBookClickListener.onClick(mList[p1].comicId)
                }
            }
            home_book_title.text = mList[p1].title
            home_book_author.text = mList[p1].subTitle
            val t = RoundedTransformationBuilder()
                .borderWidthDp(5f)
                .build()
            Picasso.get()
                .load(mList[p1].cover)
                .transform(t)
                .into(home_book_cover)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.home_item, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnBookClickListener{
        fun onClick(comicId: Long)
    }
}