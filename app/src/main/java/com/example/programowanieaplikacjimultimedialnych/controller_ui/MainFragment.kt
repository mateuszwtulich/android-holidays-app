package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.programowanieaplikacjimultimedialnych.R
import com.example.programowanieaplikacjimultimedialnych.view_model.HolidayViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : androidx.fragment.app.Fragment(), MaterialSearchBar.OnSearchActionListener {
    private lateinit var holidayViewModel: HolidayViewModel
    private lateinit var searchText: CharSequence
    private lateinit var adapter: HolidayListAdapter
    private lateinit var filter: Filter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        adapter = HolidayListAdapter(requireContext())
        filter = adapter.filter

        view.recyclerview.adapter = adapter
        view.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        holidayViewModel = ViewModelProvider(this).get(HolidayViewModel::class.java)
        holidayViewModel.allPosts.observe(requireActivity(), Observer { posts ->
            posts?.let {
                adapter.setPosts(it)
                adapter.notifyDataSetChanged()
            }
        })

        view.searchBar.setOnSearchActionListener(this)

        view.searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                searchText = charSequence
                if (searchBar.isSearchEnabled)
                    filter.filter(charSequence)

            }

            override fun afterTextChanged(editable: Editable) {}
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

    override fun onSearchStateChanged(enabled: Boolean) {}

    override fun onSearchConfirmed(text: CharSequence?) {
        searchBar.disableSearch()
        searchBar.setPlaceHolder(searchText)
    }

    override fun onButtonClicked(buttonCode: Int) {
        when (buttonCode) {
            //MaterialSearchBar.BUTTON_NAVIGATION -> 0
            //MaterialSearchBar.BUTTON_SPEECH -> 0
            MaterialSearchBar.BUTTON_BACK -> searchBar.disableSearch()
        }
    }
}
