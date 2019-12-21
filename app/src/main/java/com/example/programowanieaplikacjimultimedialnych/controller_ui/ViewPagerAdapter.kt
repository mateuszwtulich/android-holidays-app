
package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso

class ViewPagerAdapter internal constructor(private val context: Context, private val imageUrls: List<Uri>,private val onPostListner: HolidayListAdapter.OnPostListner?,private val position1: Int) :
    PagerAdapter(){


    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)

        Picasso.get()
            .load(imageUrls[position])
            .fit()
            .centerCrop()
            .into(imageView)
        container.addView(imageView)

        imageView.setOnClickListener {
            onPostListner?.onPostClick(position1)
        }

        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}