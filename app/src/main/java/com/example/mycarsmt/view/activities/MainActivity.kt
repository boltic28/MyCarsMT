package com.example.mycarsmt.view.activities

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.mycarsmt.R
import com.example.mycarsmt.model.repo.car.CarServiceImpl
import com.example.mycarsmt.view.fragments.MainListFragment

class MainActivity : AppCompatActivity() {

    private val TAG = "testmt"

    private lateinit var fragmentManager: FragmentManager

    fun getMainFragmentManager() = fragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "MAIN: new start-------------------------------------------")

        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, MainListFragment.getInstance(), MainListFragment.FRAG_TAG)
            .commit()
    }



}