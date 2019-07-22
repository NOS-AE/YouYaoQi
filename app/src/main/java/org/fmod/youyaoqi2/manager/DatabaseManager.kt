package org.fmod.youyaoqi2.manager

import org.fmod.youyaoqi2.bean.search.SearchSuggestion
import org.litepal.LitePal
import org.litepal.extension.findAll
import java.util.*

class DatabaseManager {
    companion object {
        const val TAG = "MyApp"
    }

    fun getSearchHistory(): ArrayList<SearchSuggestion>{
        return (LitePal.findAll<SearchSuggestion>() as ArrayList<SearchSuggestion>).apply {
            forEach {
                it.isHistory = true
            }
            sortByDescending {
                it.searchDate
            }
        }
    }

    fun updateHistory(str: String){
        if(LitePal.isExist(SearchSuggestion::class.java, "text = ?", str)){
            //存在条目则更新日期
            SearchSuggestion(Date()).updateAll("text = ?", str)
        }else {
            //不存在则添加
            SearchSuggestion(str, Date()).save()
        }
    }

}