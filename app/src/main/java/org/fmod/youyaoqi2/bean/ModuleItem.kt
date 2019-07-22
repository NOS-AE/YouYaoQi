package org.fmod.youyaoqi2.bean

class ModuleItem{
    var comicId = 0L
    lateinit var cover: String
    lateinit var subTitle: String
    lateinit var title: String

    override fun toString(): String {
        return "comicId:$comicId cover:$cover subTitle:$subTitle title:$title"
    }
}