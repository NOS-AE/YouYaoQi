package org.fmod.youyaoqi2.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import org.fmod.youyaoqi2.R

abstract class BaseActivity: AppCompatActivity(){
    companion object {
        const val TAG = "MyApp"
        const val UPDATE_INTERVAL = 1000*60*30
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    /**
     * @param fadeInOut 渐入渐出
     * */
    protected fun <T>startActivity(cls: Class<T>, fadeInOut: Boolean = true){
        val newIntent = Intent(this,cls)
        startActivity(newIntent)
        if(fadeInOut){
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }
    }

    protected fun toast(msg: String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

    protected fun log(msg: String){
        Log.d(TAG,msg)
    }

    protected fun transparentStatusBar(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

    protected fun checkNetworkState(): Boolean{
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val capabilities = cm.getNetworkCapabilities(network)
        return capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
    }

    protected fun getStatusBarHeight(): Int{
        val id = resources.getIdentifier("status_bar_height","dimen","android")
        return if(id > 0){
            resources.getDimensionPixelSize(id)
        }else {
            0
        }
    }
}