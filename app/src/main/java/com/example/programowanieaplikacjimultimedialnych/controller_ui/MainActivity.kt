package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.programowanieaplikacjimultimedialnych.view_model.dto.PostDtoInput
import com.example.programowanieaplikacjimultimedialnych.R
import com.example.programowanieaplikacjimultimedialnych.view_model.HolidayViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.mancj.materialsearchbar.MaterialSearchBar
import android.text.Editable
import android.text.TextUtils.replace
import android.view.WindowManager
import android.widget.Filter
import androidx.core.view.OneShotPreDrawListener.add
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


class MainActivity : AppCompatActivity(){
//    private val newPostActivityRequestCode = 1
//    private lateinit var holidayViewModel: HolidayViewModel
//    private lateinit var searchText: CharSequence
//
//    private lateinit var adapter: HolidayListAdapter
//    private lateinit var filter: Filter
//    private lateinit var searchBar: MaterialSearchBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.programowanieaplikacjimultimedialnych.R.layout.activity_main)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        replaceFragment(MainFragment.newInstance())
    }
//        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
//        val fab = findViewById<FloatingActionButton>(R.id.fab)
//        val homeButton = findViewById<ExtendedFloatingActionButton>(R.id.homeButton)
//
//        searchBar = findViewById(R.id.searchBar)
//        adapter = HolidayListAdapter(this)
//        filter = adapter.filter
//
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        holidayViewModel = ViewModelProvider(this).get(HolidayViewModel::class.java)
//        holidayViewModel.allPosts.observe(this, Observer { posts ->
//            // Update the cached copy of the words in the adapter.
//            posts?.let {
//                adapter.setPosts(it)
//                adapter.notifyDataSetChanged()
//            }
//        })
//
//        searchBar.setOnSearchActionListener(baseContext)
//
//        searchBar.addTextChangeListener(object : TextWatcher {
//            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//
//            }
//
//            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//                searchText = charSequence
//                if (searchBar.isSearchEnabled)
//                    filter.filter(charSequence)
//
//            }
//
//            override fun afterTextChanged(editable: Editable) {}
//        })
//
//        fab.setOnClickListener {
//            val intent = Intent(this@MainActivity, NewPostActivity::class.java)
//            startActivityForResult(intent, newPostActivityRequestCode)
//        }
//
//        homeButton.setOnClickListener {
//            recyclerView.stopScroll()
//            recyclerView.layoutManager?.scrollToPosition(0)
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == newPostActivityRequestCode && resultCode == Activity.RESULT_OK) {
//            val title = data?.getStringExtra("title")
//            val text = data?.getStringExtra("text")
//            val uri = data?.getStringArrayListExtra("image")
//
//            if (title != null && text != null && uri != null) {
//                val post = PostDtoInput(
//                    id = 0,
//                    title = title,
//                    text = text,
//                    uriList = uri
//                )
//
//                GlobalScope.launch { holidayViewModel.insert(post) }
//            }
//        } else {
//            Toast.makeText(
//                applicationContext,
//                R.string.empty_not_saved,
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }
//
//    override fun onSearchStateChanged(enabled: Boolean) {}
//
//    override fun onSearchConfirmed(text: CharSequence?) {
//        searchBar.disableSearch()
//        searchBar.setPlaceHolder(searchText)
//    }
//
//    override fun onButtonClicked(buttonCode: Int) {
//        when (buttonCode) {
//            //MaterialSearchBar.BUTTON_NAVIGATION -> 0
//            //MaterialSearchBar.BUTTON_SPEECH -> 0
//            MaterialSearchBar.BUTTON_BACK -> searchBar.disableSearch()
//        }
//    }

    fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
