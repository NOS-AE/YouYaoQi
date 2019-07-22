package org.fmod.youyaoqi2.bean.details

import com.google.gson.annotations.SerializedName

class ChapterDetails {
    lateinit var name: String
    @SerializedName("chapter_id")
    lateinit var chapterId: String
    @SerializedName("vip_images")
    lateinit var vipImages: String
    @SerializedName("publish_time")
    var publishTime = 0L
    var chapterIndex = 0
    lateinit var smallPlaceCover: String
    lateinit var index: String

}