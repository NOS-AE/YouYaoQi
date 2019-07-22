package org.fmod.youyaoqi2.bean

import java.lang.StringBuilder

class HomeBean{
    lateinit var defaultSearch: String
    lateinit var modules: ArrayList<ModuleBean>

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("$defaultSearch\n")
        for(i in modules) {
            builder.append(i)
            builder.append("\n")
        }
        return builder.toString()
    }
}