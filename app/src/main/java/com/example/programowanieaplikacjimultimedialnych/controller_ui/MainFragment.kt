package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.programowanieaplikacjimultimedialnych.R
import com.example.programowanieaplikacjimultimedialnych.view_model.HolidayViewModel
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.lang.Exception
import java.util.*

class MainFragment : androidx.fragment.app.Fragment(), MaterialSearchBar.OnSearchActionListener {
    private lateinit var holidayViewModel: HolidayViewModel
    private lateinit var searchText: CharSequence
    private lateinit var adapter: HolidayListAdapter
    private lateinit var filter: Filter
    private val REQUEST_CODE_SPEACH_INPUT = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        adapter = HolidayListAdapter(requireContext())
        filter = adapter.filter

        view.recyclerview.adapter = adapter
        view.recyclerview.layoutManager = LinearLayoutManager(view.context)

        holidayViewModel = ViewModelProvider(this).get(HolidayViewModel::class.java)
        holidayViewModel.allPosts.observe(requireActivity(), Observer { posts ->
            posts?.let {
                adapter.setPosts(it)
                adapter.notifyDataSetChanged()
            }
        })

        view.searchBar.setOnSearchActionListener(this)

        view.searchBar.placeHolderView.setTypeface(null, Typeface.NORMAL)
        view.searchBar.setCardViewElevation(0)

        searchText = ""
        view.searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if(searchBar.isSearchEnabled)
                    searchText = charSequence

            }

            override fun afterTextChanged(editable: Editable) {
                if(searchBar.isSearchEnabled)
                    filter.filter(editable)
            }
        })

        view.fab.setOnClickListener {
            (activity as MainActivity).replaceFragment(NewPostFragment.newInstance())
        }

        view.homeButton.setOnClickListener {
            view.recyclerview.stopScroll()
            view.recyclerview.layoutManager?.scrollToPosition(0)
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    private fun speach(){
        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        try{
            startActivityForResult(mIntent,REQUEST_CODE_SPEACH_INPUT)
        }
        catch (e : Exception){
            Toast.makeText(context,e.message, Toast.LENGTH_LONG).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEACH_INPUT && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                //zwraca kila możliwych odpowiedzi
                val str = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).toArray()[0] as String
                searchText = str
                onSearchConfirmed(str)
            } else
                searchText = "ERROR"
        }
    }
}
