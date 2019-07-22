package org.fmod.youyaoqi2.manager

import android.util.Log
import okhttp3.*
import org.fmod.youyaoqi2.fragment.BookDetailsFragment
import java.io.IOException
import java.net.URLEncoder
import kotlin.math.log

class NetworkManager{
    companion object {
        private const val TAG = "MyApp"

        private const val versionCode = 4600200

        private const val homeUrl = "http://app.u17.com/v3/appV3_3/android/phone/comic/getDetectListV4_5"
        private const val searchResultUrl = "http://app.u17.com/v3/appV3_3/android/phone/search/searchResult"
        private const val similarSearchUrl = ""
        private const val bookDetailUrl = "http://app.u17.com/v3/appV3_3/android/phone/comic/detail_static_new?v=$versionCode&"

        private val sClient = OkHttpClient()
    }
    var homeCallBack: HomeCallback? = null
    var searchResultCallback: SearchCallback? = null
    var bookDetailCallback: DetailCallback? = null

    fun getHomeJson(){
        if (homeCallBack == null)
            return
        val request = Request.Builder()
            .url(homeUrl)
            .build()
        sClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "home get failure: $e")

            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d(TAG, "home get successful")
                    homeCallBack?.onComplete(response.body()?.string())
                } else {
                    Log.d(TAG, "home get unsuccessful \n${response.body()?.string()}")
                }
            }
        })
    }

    fun getSearchResultJson(toSearch: String, page: Int){
        if(searchResultCallback == null)
            return
        val url = "$searchResultUrl?q=${URLEncoder.encode(toSearch,"utf-8")}&page=$page"
        Log.d(TAG,url)
        val request = Request.Builder()
            .url(url)
            .build()
        sClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "search result get failure: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d(TAG, "search result get successful")
                    searchResultCallback?.onComplete(response.body()?.string())
                } else {
                    Log.d(TAG, "search result get unsuccessful \n${response.body()?.string()}")
                }
            }
        })
    }

    fun getBookDetailJson(comicId: Long){
        if (bookDetailCallback == null)
            return
        val url = "${bookDetailUrl}comicid=$comicId"
        val request = Request.Builder()
            .url(url)
            .build()
        sClient.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG,"book details get failure: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    Log.d(TAG,"book details get successful")
                    bookDetailCallback?.onComplete(response.body()?.string())
                }else{
                    Log.d(TAG,"book details get unsuccessful \n${response.body()?.string()}")
                }
            }
        })
    }

    interface DetailCallback{
        fun onComplete(json: String?)
    }

    interface HomeCallback{
        fun onComplete(json: String?)
    }

    interface SearchCallback{
        fun onComplete(json: String?)
    }

}