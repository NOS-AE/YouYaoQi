package org.fmod.youyaoqi2.bean.details

import com.google.gson.annotations.SerializedName

class BookDetails {
    lateinit var comic: ComicDetails
    @SerializedName("chapter_list")
    lateinit var chapterList: ArrayList<ChapterDetails>
    override fun toString(): String {
        return comic.toString()
    }
}