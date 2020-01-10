package com.example.programowanieaplikacjimultimedialnych.controller_ui


import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.programowanieaplikacjimultimedialnych.R
import com.example.programowanieaplikacjimultimedialnych.view_model.dto.PostDtoOutput
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.time.format.DateTimeFormatter
import javax.sql.DataSource
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import kotlinx.android.synthetic.main.fragment_new_post.view.*


class PostFragment : Fragment() {

    private var imagePosition: Int = 0
    private var postPosition: Int = 0
    private val formater = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    interface PostFragmentListner {
        fun updatePF(bundle: Bundle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        postponeEnterTransition()
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        if (arguments != null) {
            val postDtoOutput = arguments?.getParcelable<PostDtoOutput>("post")
            val array = arguments?.getIntArray("positions")

            val title: TextView = view.findViewById(R.id.Title)
            val description: TextView = view.findViewById(R.id.description)
            val localization: TextView = view.findViewById(R.id.localization)
            val date: TextView = view.findViewById(R.id.dateText)
            val pagerView = view.findViewById<ViewPager>(R.id.PagerView)
            val indicator = view.findViewById<ScrollingPagerIndicator>(R.id.indicator)

            postPosition = array!![0]
            imagePosition = array[1]

            title.text = postDtoOutput?.title
            description.text = postDtoOutput?.text
            localization.text = postDtoOutput?.location.toString()
            date.text = postDtoOutput?.date?.format(formater)

            val adapter = ViewPagerAdapter(context!!, postDtoOutput!!.uriList, null, array[0])
            pagerView.adapter = adapter
            pagerView.offscreenPageLimit = 6


            if (postDtoOutput.uriList.count() > 1)
                indicator.visibility = View.VISIBLE
            else
                indicator.visibility = View.INVISIBLE

            indicator.attachToPager(pagerView)
            pagerView.setCurrentItem(imagePosition, false)

          //  (activity as MainActivity).scheduleStartPostponedTransition(pagerView)

        }
        return view
    }


    override fun onStart() {
        super.onStart()
        startPostponedEnterTransition()
    }

    override fun onStop() {
        super.onStop()

        val bundle = Bundle()
        val arr = intArrayOf(postPosition, view!!.findViewById<ViewPager>(R.id.PagerView).currentItem)
        bundle.putIntArray("position",arr)

        Log.d("PostFragment :", "Tab[${arr[0]},${arr[1]}]")
        (activity as MainActivity).updatePF(bundle)

    }

    companion object {
        @JvmStatic
        fun newInstance() = PostFragment()
    }

}