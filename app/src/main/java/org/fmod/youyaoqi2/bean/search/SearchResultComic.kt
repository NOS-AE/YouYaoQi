package org.fmod.youyaoqi2.bean.search

class SearchResultComic {
    var comicId = 0L
    lateinit var cover: String
    lateinit var name: String
    lateinit var author: String

    override fun toString(): String {
        return "comicId:$comicId, cover:$cover, name:$name, author:$author"
    }
}