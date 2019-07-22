package org.fmod.youyaoqi2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.fmod.youyaoqi2.R
import org.fmod.youyaoqi2.manager.DatabaseManager

class Fragment3 : BaseFragment() {

    val databaseManager = DatabaseManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val parent = inflater.inflate(R.layout.fragment_fragment3, container, false)
        return parent
    }

    fun noNetworkSearch(){

    }

    fun noResultSearch(){

    }

}
