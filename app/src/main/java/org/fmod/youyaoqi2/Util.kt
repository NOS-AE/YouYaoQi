package org.fmod.youyaoqi2

import android.content.res.Resources

class Util {
    companion object {
        fun dp2px(dp: Int): Int{
            val density = Resources.getSystem().displayMetrics.density
            return (dp * density + 0.5).toInt()
        }
    }
}