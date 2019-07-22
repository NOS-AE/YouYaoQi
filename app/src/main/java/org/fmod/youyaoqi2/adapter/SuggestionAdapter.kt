package org.fmod.youyaoqi2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.suggestion_item.view.*
import org.fmod.youyaoqi2.R
import org.fmod.youyaoqi2.ViewHolder
import org.fmod.youyaoqi2.bean.search.SearchSuggestion

class SuggestionAdapter(var mList: ArrayList<SearchSuggestion>): RecyclerView.Adapter<ViewHolder>(){

    var suggestionClickListener: OnSuggestionClickListener? = null
    var suggestionDeleteListener: OnSuggestionDeleteListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.suggestion_item,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.run {
            suggestion_container.setOnClickListener {
                suggestionClickListener?.onClick(mList[position])
            }

            suggestion_text.setCompoundDrawablesRelativeWithIntrinsicBounds(
                if(mList[position].isHistory)
                    R.drawable.ic_history
                else
                    R.drawable.ic_search_grey
                ,0,0,0
            )

            suggestion_delete.setOnClickListener {
                suggestionDeleteListener?.onClick(mList[position])
                mList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
            }

            suggestion_text.text = mList[position].text
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnSuggestionClickListener{
        fun onClick(query: SearchSuggestion)
    }

    interface OnSuggestionDeleteListener{
        fun onClick(item: SearchSuggestion)
    }
}