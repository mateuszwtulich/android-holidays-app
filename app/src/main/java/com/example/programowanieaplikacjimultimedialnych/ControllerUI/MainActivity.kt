package com.example.programowanieaplikacjimultimedialnych.ControllerUI

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.programowanieaplikacjimultimedialnych.ViewModel.DTO.PostDtoInput
import com.example.programowanieaplikacjimultimedialnych.R
import com.example.programowanieaplikacjimultimedialnych.database.HolidayViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val newPostActivityRequestCode = 1
    private lateinit var holidayViewModel: HolidayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = HolidayListAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        holidayViewModel = ViewModelProvider(this).get(HolidayViewModel::class.java)

        holidayViewModel.allPosts.observe(this, Observer { posts ->
            // Update the cached copy of the words in the adapter.
            posts?.let {adapter.setPosts(it)}
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewPostActivity::class.java)
            startActivityForResult(intent, newPostActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newPostActivityRequestCode && resultCode == Activity.RESULT_OK) {
                val title = data?.getStringExtra("title")
                val text = data?.getStringExtra("text")
                val uri = data?.getStringExtra("image")

                if(title != null && text != null && uri != null){
                    val noNullTitle = title
                    val noNullText = text
                    val post = PostDtoInput(
                        id = 0,
                        title = noNullTitle,
                        text = noNullText,
                        uriList = listOf(Uri.parse(uri))
                    )
                    holidayViewModel.insert(post)
                }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }
}
