package org.fmod.youyaoqi2.fragment

import androidx.fragment.app.Fragment
import android.util.Log
import android.widget.Toast

abstract class BaseFragment: androidx.fragment.app.Fragment() {

    companion object {
        private const val TAG = "MyApp"
    }

    protected fun toast(msg: String){
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    protected fun log(msg: String){
        Log.d(TAG, msg)
    }
}