package com.example.programowanieaplikacjimultimedialnych.controller_ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.programowanieaplikacjimultimedialnych.R
import com.example.programowanieaplikacjimultimedialnych.view_model.dto.PostDtoOutput
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.time.format.DateTimeFormatter

class PostFragment: Fragment(){


    private val formater = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        if (arguments != null){
            val postDtoOutput = arguments?.getParcelable<PostDtoOutput>("post")
            val array = arguments?.getIntArray("positions")

            val title : TextView = view.findViewById(R.id.Title)
            val text : TextView = view.findViewById(R.id.TextContnet)
            val localisation : TextView = view.findViewById(R.id.localistaionText)
            val date : TextView = view.findViewById(R.id.dateText)
            val pagerView = view.findViewById<ViewPager>(R.id.PagerView)
            val indicator = view.findViewById<ScrollingPagerIndicator>(R.id.indicator)

            title.text = postDtoOutput?.title
            text.text = postDtoOutput?.text
            localisation.text = postDtoOutput?.location.toString()
            date.text = postDtoOutput?.date?.format(formater)

            val adapter = ViewPagerAdapter(context!!, postDtoOutput!!.uriList ,null, array!![0])
            pagerView.adapter = adapter

            if(postDtoOutput.uriList.count() > 1)
                indicator.visibility = View.VISIBLE
            else
                indicator.visibility = View.INVISIBLE
            indicator.attachToPager(pagerView)
            pagerView.currentItem = array[1]
        }

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() = PostFragment()
    }

}