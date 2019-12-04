package com.example.programowanieaplikacjimultimedialnych

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.programowanieaplikacjimultimedialnych.database.HolidayViewModel
import com.example.programowanieaplikacjimultimedialnych.database.PostDto
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

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.HORIZONTAL
            )
        )

        holidayViewModel = ViewModelProvider(this).get(HolidayViewModel::class.java)
        holidayViewModel.allPosts.observe(this, Observer { posts ->
            // Update the cached copy of the words in the adapter.
            posts?.let {adapter.setPosts(it) }
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

                if(title != null && text != null){
                    val noNullTitle = title
                    val noNullText = text
                    val post = PostDto(
                        id = 0,
                        title = noNullTitle,
                        text = noNullText
                    )
                    holidayViewModel.insert(post)
                }
            //}

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }
}
