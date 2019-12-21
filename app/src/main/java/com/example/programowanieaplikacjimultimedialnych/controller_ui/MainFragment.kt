package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.*
import com.example.programowanieaplikacjimultimedialnych.R
import com.example.programowanieaplikacjimultimedialnych.view_model.HolidayViewModel
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.lang.Exception
import java.util.*

class MainFragment : androidx.fragment.app.Fragment(), MaterialSearchBar.OnSearchActionListener,
    HolidayListAdapter.OnPostListner {

    private val REQUEST_CODE_SPEACH_INPUT = 100
    private var searchText: CharSequence = ""
    private lateinit var holidayViewModel: HolidayViewModel
    private lateinit var adapter: HolidayListAdapter
    private lateinit var filter: Filter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        adapter = HolidayListAdapter(requireContext(), this)
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
        view.searchBar.placeHolderView.ellipsize = TextUtils.TruncateAt.END

        view.searchBar.placeHolderView.setTypeface(null, Typeface.NORMAL)
        view.searchBar.setCardViewElevation(0)

        view.searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (searchBar.isSearchEnabled)
                    searchText = charSequence
            }

            override fun afterTextChanged(editable: Editable) {
                if (searchBar.isSearchEnabled)
                    filter.filter(editable)
                searchBar.setPlaceHolder(searchText.toString())
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

    private fun speach() {
        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        try {
            startActivityForResult(mIntent, REQUEST_CODE_SPEACH_INPUT)
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        if (enabled) {
            searchBar.text = searchText.toString()
            searchBar.searchEditText.setSelection(searchText.length)
        }

    }

    override fun onSearchConfirmed(text: CharSequence?) {
        searchBar.disableSearch()
        filter.filter(searchText)
    }

    override fun onButtonClicked(buttonCode: Int) {
        when (buttonCode) {
            MaterialSearchBar.BUTTON_SPEECH -> speach()
            MaterialSearchBar.BUTTON_BACK -> searchBar.disableSearch()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEACH_INPUT && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val str = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).toArray()[0] as String
                searchText = str
                onSearchConfirmed(str)
            }
        }
    }

    override fun onPostClick(position: Int) {
        val post = holidayViewModel.allPosts.value?.reversed()?.get(position)

        val fragment = PostFragment.newInstance()

        fragment.arguments = Bundle()
        fragment.arguments?.putParcelable("post",post)

        fragment.sharedElementEnterTransition = DetailsTransition()
        fragment.sharedElementReturnTransition = DetailsTransition()
        fragment.enterTransition = Fade()
        exitTransition = Fade()

        activity!!.supportFragmentManager
            .beginTransaction()
            .addSharedElement(recyclerview.findViewHolderForAdapterPosition(position)?.itemView!!.findViewById(R.id.PagerView),"PagerView")
            .replace(R.id.fragment_container,fragment)
            .addToBackStack(null)
            .commit()
    }

    inner class DetailsTransition: TransitionSet() {
       init{
           setOrdering(ORDERING_TOGETHER)
           addTransition(ChangeBounds())
               .addTransition(ChangeTransform())
               .addTransition(ChangeImageTransform())
       }
    }
}

