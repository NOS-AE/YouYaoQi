package org.fmod.youyaoqi2.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.fmod.youyaoqi2.R
import org.fmod.youyaoqi2.bean.ModuleItem
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class Fragment2 : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        EventBus.getDefault().register(this)
        // Inflate the layout for this fragment
        log("onCreateView")
        return inflater.inflate(R.layout.fragment_fragment2, container, false)
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        log("onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        log("onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        log("onDestroy")
        super.onDetach()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun getHomeBean(moduleList: List<ModuleItem>){

    }

}
