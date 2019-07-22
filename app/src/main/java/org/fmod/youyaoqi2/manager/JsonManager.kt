package org.fmod.youyaoqi2.manager

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.fmod.youyaoqi2.bean.details.BookDetails
import org.fmod.youyaoqi2.bean.HomeBean
import org.fmod.youyaoqi2.bean.search.SearchResult
import org.json.JSONArray
import org.json.JSONObject

class JsonManager{
    companion object {
        private const val TAG = "MyApp"

        private val gson = Gson()

        fun parseHomeJson(json: String): HomeBean {
            val returnData = JSONObject(json)
                .getJSONObject("data")
                .getJSONObject("returnData")
            val ret = gson.fromJson<HomeBean>(returnData.toString())

            /*
            * JsonObject解析items
            * returnData对象:modules数组:items数组:元素类型可能对象或数组，只留下数组
            * */
            val modules = returnData.getJSONArray("modules")
            //遍历modules
            var j = 0
            for(i in 0 until modules.length()){
                val itemJson = (modules[i] as JSONObject).getJSONArray("items")
                //有妖气中非数组items都不是本子，只保留数组items
                if(itemJson[0] is JSONArray) {
                    ret.modules[j].items = gson.fromJson(itemJson.toString())
                    j++
                }
                else {
                    ret.modules.removeAt(j)
                }
            }
            Log.d(TAG,"home bean parse has finished")
            return ret
        }

        fun parseSearchResult(json: String): SearchResult{
            val returnData = JSONObject(json)
                .getJSONObject("data")
                .getJSONObject("returnData")
            return gson.fromJson<SearchResult>(returnData.toString())
        }

        fun parseBookDetails(json: String): BookDetails {
            val returnData = JSONObject(json)
                .getJSONObject("data")
                .getJSONObject("returnData")
            val res = gson.fromJson<BookDetails>(returnData.toString())
            return res
        }

        private inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)
    }
}