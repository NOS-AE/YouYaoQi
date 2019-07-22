package org.fmod.youyaoqi2.bean.search

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.util.*

class SearchSuggestion(): LitePalSupport() {
    @Column(unique = true)
    var id = 0
    @Column(unique = true)
    lateinit var text: String
    lateinit var searchDate: Date
    @Column(ignore = true)
    var isHistory = false

    constructor(text: String, date: Date): this(){
        this.text = text
        searchDate = date
    }

    constructor(text: String):this(){
        this.text = text
    }

    constructor(date: Date):this(){
        searchDate = date
    }

}