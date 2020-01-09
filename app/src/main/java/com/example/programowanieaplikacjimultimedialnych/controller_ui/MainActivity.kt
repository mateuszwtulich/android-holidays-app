package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.programowanieaplikacjimultimedialnych.R
import android.view.WindowManager
import androidx.fragment.app.Fragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(git R.layout.activity_main)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        replaceFragment(MainFragment.newInstance())
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}