package org.fmod.youyaoqi2.bean

import com.google.gson.annotations.SerializedName
import java.lang.StringBuilder

class ModuleBean{
    var moduleType = 0
    //items采用JsonObject解析，所以要故意将对应的key改成与json中的不同
    @SerializedName("mItems")
    lateinit var items: ArrayList<ArrayList<ModuleItem>>

    override fun toString(): String {
        val builder = StringBuilder()
        for(i in items){
            for(j in i){
                builder.append("\n$j")
            }
        }
        return builder.toString()
    }
}