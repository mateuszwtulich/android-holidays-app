package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.programowanieaplikacjimultimedialnych.R


class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.programowanieaplikacjimultimedialnych.R.layout.activity_main)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MainFragment.newInstance())
            .commit()
    }

    fun replaceFragment(fragment: Fragment){
       supportFragmentManager.beginTransaction()
           .replace(R.id.fragment_container, fragment)
           .addToBackStack(null)
           .commit()
    }

}
