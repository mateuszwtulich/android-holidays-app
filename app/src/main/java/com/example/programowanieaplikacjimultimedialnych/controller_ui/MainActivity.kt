package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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


class MainActivity : AppCompatActivity() {

    private val newPostActivityRequestCode = 1
    private lateinit var holidayViewModel: HolidayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.programowanieaplikacjimultimedialnych.R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = HolidayListAdapter(this)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        val homeButton = findViewById<ExtendedFloatingActionButton>(R.id.homeButton)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        holidayViewModel = ViewModelProvider(this).get(HolidayViewModel::class.java)

        holidayViewModel.allPosts.observe(this, Observer { posts ->
            // Update the cached copy of the words in the adapter.
            posts?.let {adapter.setPosts(it)
                        adapter.notifyDataSetChanged()}
        })

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewPostActivity::class.java)
            startActivityForResult(intent, newPostActivityRequestCode)
        }

        homeButton.setOnClickListener {
            recyclerView.stopScroll()
            recyclerView.layoutManager?.scrollToPosition(0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newPostActivityRequestCode && resultCode == Activity.RESULT_OK) {
                val title = data?.getStringExtra("title")
                val text = data?.getStringExtra("text")
                val uri  = data?.getStringArrayListExtra("image")

                if(title != null && text != null && uri != null){

                    val post = PostDtoInput(
                        id = 0,
                        title = title,
                        text = text,
                        uriList = uri
                    )
                    GlobalScope.launch {  holidayViewModel.insert(post)}
                }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }
}
