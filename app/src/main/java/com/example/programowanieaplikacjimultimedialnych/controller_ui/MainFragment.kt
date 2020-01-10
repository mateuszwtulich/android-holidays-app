
package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.*
import androidx.viewpager.widget.ViewPager
import com.example.programowanieaplikacjimultimedialnych.R
import com.example.programowanieaplikacjimultimedialnych.view_model.HolidayViewModel
import com.mancj.materialsearchbar.MaterialSearchBar
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.lang.Exception
import java.util.*

class MainFragment : Fragment(), MaterialSearchBar.OnSearchActionListener,
    HolidayListAdapter.OnPostListner {

    private val REQUEST_CODE_SPEACH_INPUT = 100
    private var searchText: CharSequence = ""
    private lateinit var holidayViewModel: HolidayViewModel
    private lateinit var adapter: HolidayListAdapter
    private lateinit var filter: Filter

    private var currentPost: Int = 0
    private var currentImage: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        postponeEnterTransition()

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        adapter = HolidayListAdapter(requireContext(), this)
        adapter.setHasStableIds(true)

        filter = adapter.filter

        view.recyclerview.adapter = adapter
        view.recyclerview.layoutManager = LinearLayoutManager(view.context)

        view.recyclerview.setHasFixedSize(true)
        view.recyclerview.setItemViewCacheSize(15)


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

        if (searchText != "")
            filter.filter(searchText)

        view.searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (view.searchBar.isSearchEnabled)
                    searchText = charSequence
            }

            override fun afterTextChanged(editable: Editable) {
                if (view.searchBar.isSearchEnabled)
                    filter.filter(editable)
                view.searchBar.setPlaceHolder(searchText.toString())

            }
        })

        view.fab.setOnClickListener {
            (activity as MainActivity).replaceFragment(NewPostFragment.newInstance())
        }

        view.homeButton.setOnClickListener {

            view.recyclerview.stopScroll()
            view.recyclerview.layoutManager?.smoothScrollToPosition(view.recyclerview,RecyclerView.State(), 0)

        }
        startPostponedEnterTransition()
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
        try {
            startActivityForResult(mIntent, REQUEST_CODE_SPEACH_INPUT)
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()

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
            MaterialSearchBar.BUTTON_SPEECH -> speach()
            MaterialSearchBar.BUTTON_BACK -> searchBar.disableSearch()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEACH_INPUT && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val str = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).toArray()!![0] as String
                searchText = str
                onSearchConfirmed(str)
            }
        }
    }

    override fun onPostClick(position: Int, image: Int) {
        val post = adapter.getFilterdPost(position)
        val fragment = PostFragment.newInstance()
        searchBar.disableSearch()

        //Dane
        fragment.arguments = Bundle()
        fragment.arguments?.putParcelable("post", post)
        fragment.arguments?.putIntArray("positions", intArrayOf(position, image))

        //Animacja
        fragment.sharedElementEnterTransition = DetailsTransition()
        fragment.sharedElementReturnTransition = DetailsTransition()
        fragment.enterTransition = Fade()
        exitTransition = Fade()

        //Wspódzielony element
        val view = recyclerview.findViewHolderForAdapterPosition(position)?.itemView
            ?.findViewById<ViewPager>(R.id.PagerView)!!.findViewWithTag<ImageView>("image$image")

        //Start Fragmentu z animacją

        (activity as MainActivity).replaceFragmentWithAnimation(fragment, view, "trans_($position,$image)")

    }

    //Klasa animacji
    inner class DetailsTransition : TransitionSet() {
        init {
            ordering = ORDERING_TOGETHER
            addTransition(ChangeTransform())
            addTransition(ChangeImageTransform())
            addTransition(ChangeBounds())
        }
    }

    fun updateData(bundle: Bundle){
        Log.d("Main Fragment:" ,bundle.toString())
        currentPost = bundle.getIntArray("position")!![0]
        currentImage = bundle.getIntArray("position")!![1]

        view?.recyclerview?.findViewHolderForAdapterPosition(currentPost)
            ?.itemView?.findViewById<ViewPager>(R.id.PagerView)?.setCurrentItem(currentImage,false)
    }

}
