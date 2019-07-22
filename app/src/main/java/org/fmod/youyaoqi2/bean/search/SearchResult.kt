package org.fmod.youyaoqi2.bean.search

import java.lang.StringBuilder

class SearchResult {
    var hasMore = true
    var comicNum = 0
    var page = 0
    lateinit var comics: ArrayList<SearchResultComic>
    //用于searchFragment加载更多
    lateinit var toSearch: String

    override fun toString(): String {
        val ret = StringBuilder("hasMore:$hasMore, comicNum:$comicNum, page:$page, comics:")
        comics.forEach {
            ret.append(it.toString())
        }
        return ret.toString()
    }
}