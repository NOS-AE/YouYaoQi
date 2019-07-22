package org.fmod.youyaoqi2.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.fmod.youyaoqi2.fragment.Fragment1
import org.fmod.youyaoqi2.fragment.Fragment2
import org.fmod.youyaoqi2.fragment.Fragment3
import org.fmod.youyaoqi2.fragment.Fragment4

class MyFragmentPagerAdapter(fm: androidx.fragment.app.FragmentManager): androidx.fragment.app.FragmentPagerAdapter(fm) {

    private val titles = arrayOf("首页","发现","进货单","我的")

    override fun getCount(): Int {
        return titles.size
    }


    override fun getItem(p0: Int): androidx.fragment.app.Fragment {
        return when(p0){
            0->{
               Fragment1()
            }
            1->{
                Fragment2()
            }
            2->{
                Fragment3()
            }
            else->{
                Fragment4()
            }
        }
    }
}