package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.content.Intent
import android.location.Geocoder
import android.app.Application
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.programowanieaplikacjimultimedialnych.R
import com.example.programowanieaplikacjimultimedialnych.view_model.HolidayViewModel
import com.example.programowanieaplikacjimultimedialnych.view_model.dto.PostDtoOutput
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.android.parcel.Parcelize
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.time.format.DateTimeFormatter

import kotlinx.coroutines.launch

class PostFragment : Fragment(),BottomSheetDialog.Sheet {

    private var imagePosition: Int = 0
    private var postPosition: Int = 0
    private val holidayViewModel: HolidayViewModel = HolidayViewModel(application = Application())
    lateinit var postDtoOutput: PostDtoOutput
    lateinit var bottomSheetDialog: BottomSheetDialog
    private val formater = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    interface PostFragmentListner {
        fun updatePF(bundle: Bundle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        postponeEnterTransition()
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        if (arguments != null) {
            postDtoOutput = arguments?.getParcelable("post")!!
            val array = arguments?.getIntArray("positions")

            val title: TextView = view.findViewById(R.id.Title)
            val description: TextView = view.findViewById(R.id.description)
            val localization: TextView = view.findViewById(R.id.localization)
            val date: TextView = view.findViewById(R.id.dateText)
            val pagerView = view.findViewById<ViewPager>(R.id.PagerView)
            val indicator = view.findViewById<ScrollingPagerIndicator>(R.id.indicator)
            val closeButton = view.findViewById<ImageButton>(R.id.closeButton)
            val expandButton = view.findViewById<ImageButton>(R.id.expandMenu)


            closeButton.setOnClickListener {
                Thread.sleep(150)
               (activity as MainActivity).supportFragmentManager.popBackStack()
            }


            postPosition = array!![0]
            imagePosition = array[1]

            title.text = postDtoOutput?.title
            description.text = postDtoOutput?.text
            val geocoder = Geocoder(context)
            val adresses = geocoder.getFromLocation(postDtoOutput?.location!!.latitude, postDtoOutput?.location.longitude, 1)[0]
            localization.text = adresses.getAddressLine(0).toString()
            date.text = postDtoOutput?.date?.format(formater)

            val adapter = ViewPagerAdapter(context!!, postDtoOutput.uriList, null, array[0])
            pagerView.adapter = adapter
            pagerView.offscreenPageLimit = 6


            if (postDtoOutput.uriList.count() > 1)
                indicator.visibility = View.VISIBLE
            else
                indicator.visibility = View.INVISIBLE

            indicator.attachToPager(pagerView)
            pagerView.setCurrentItem(imagePosition, false)

            (activity as MainActivity).scheduleStartPostponedTransition(view.findViewById<ViewPager>(R.id.PagerView))

            view.locationsMap.setOnClickListener {
                val intent = Intent(getActivity(), LocationsOnMapActivity::class.java)
                intent.putExtra("postsList", arguments?.getParcelableArrayList<PostDtoOutput>("postsList"))
                intent.putExtra("markerOptions",  MarkerOptions().position(
                    LatLng(postDtoOutput?.location!!.latitude, postDtoOutput?.location.longitude)).title(adresses.getAddressLine(0).toString()))
                startActivityForResult(intent, LOCATION_CODE)
            }

            expandButton.setOnClickListener {
                bottomSheetDialog  =  BottomSheetDialog()

                bottomSheetDialog.show(activity!!.supportFragmentManager,"example")
            }

          //  (activity as MainActivity).scheduleStartPostponedTransition(pagerView)

        }
        return view
    }

    override fun delete() {
        GlobalScope.launch {
            holidayViewModel.deletePost(postDtoOutput)

        }
        bottomSheetDialog.dismiss()
        val manager = (activity as  MainActivity).supportFragmentManager
        manager.popBackStackImmediate()
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
        private const val LOCATION_CODE = 1002
    }

}