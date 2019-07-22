package org.fmod.youyaoqi2.bean.details

import com.google.gson.annotations.SerializedName

class ComicDetails{
    lateinit var name: String
    @SerializedName("comic_id")
    var comicId = 0L
    lateinit var cover: String
    lateinit var description: String
    lateinit var wideCover: String
    lateinit var wideColor: String
    lateinit var author: AuthorDetails
    override fun toString(): String {
        return "comicId:$comicId, description:$description, wideCover:$wideCover"
    }
}