package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.LocaleList
import android.speech.RecognizerIntent
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
import android.util.Log
import android.view.View
import android.widget.Filter
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class MainActivity : AppCompatActivity(), MaterialSearchBar.OnSearchActionListener {
    private val newPostActivityRequestCode = 1
    private val REQUEST_CODE_SPEACH_INPUT = 100

    private lateinit var holidayViewModel: HolidayViewModel
    private lateinit var searchText : CharSequence

    private  lateinit var adapter : HolidayListAdapter
    private lateinit var filter : Filter
    private lateinit var searchBar : MaterialSearchBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.programowanieaplikacjimultimedialnych.R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        val homeButton : ExtendedFloatingActionButton  = findViewById(R.id.homeButton)

        searchBar =  findViewById(R.id.searchBar)
        searchBar.placeHolderView.setTypeface(null,Typeface.NORMAL)
        searchBar.setCardViewElevation(0)

        searchText = ""

        adapter = HolidayListAdapter(this)
        filter = adapter.filter

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        holidayViewModel = ViewModelProvider(this).get(HolidayViewModel::class.java)
        holidayViewModel.allPosts.observe(this, Observer { posts ->
            // Update the cached copy of the words in the adapter.
            posts?.let {adapter.setPosts(it)
                        adapter.notifyDataSetChanged()}
        })

        searchBar.setOnSearchActionListener(this)
        searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(searchBar.isSearchEnabled)
                    searchText = charSequence
            }
            override fun afterTextChanged(editable: Editable) {
                if(searchBar.isSearchEnabled)
                    filter.filter(editable)
            }
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

    private fun speach(){
        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        try{
            startActivityForResult(mIntent,REQUEST_CODE_SPEACH_INPUT)
        }
        catch (e : Exception){
            Toast.makeText(this,e.message, Toast.LENGTH_LONG).show()
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
                        uriList = uri)

                    GlobalScope.launch {  holidayViewModel.insert(post)}
                }
        }else if(requestCode == REQUEST_CODE_SPEACH_INPUT && resultCode == Activity.RESULT_OK){
            if(data != null){
                //zwraca kila możliwych odpowiedzi
                var str = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).toArray()[0] as String
//                val array = arrayOfNulls<String>(list.size)
//                list.toArray(array)
//                var str = ""
//                array.forEach {x -> str+=(x + " ")}
//                Log.v(null,str)
                searchText = str
                onSearchConfirmed(str)
            }
            else
                searchText = "ERROR"
        }
        else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        if(enabled){
            searchBar.text = searchText.toString()
            searchBar.searchEditText.setSelection(searchText.length)
        }
        else
            searchBar.setPlaceHolder(searchText.toString())
    }

    //może jak za długie to trzy kropki
    //czy potrzebne sugestie ?
    //jeśli nie znaleziono to nie zapisywać sugestji
    //optymalizacja w poście dto może jedanak trzymać stringa zamiast daty i parsować tylko przy dodoawnaiu ???
    //zapamiętać text po ponownym wyszukiwaniu
    // nav icon zamienić na search icon ???
    //onResume  on Pause???
    override fun onSearchConfirmed(text: CharSequence?) {
        searchBar.disableSearch()
        filter.filter(searchText)
    }

    override fun onButtonClicked(buttonCode: Int) {
        when (buttonCode) {
            //MaterialSearchBar.BUTTON_NAVIGATION -> 0
            MaterialSearchBar.BUTTON_SPEECH -> speach()
            MaterialSearchBar.BUTTON_BACK -> searchBar.disableSearch()
        }
    }
}
