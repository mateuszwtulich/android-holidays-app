package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.programowanieaplikacjimultimedialnych.R
import com.example.programowanieaplikacjimultimedialnych.view_model.dto.PostDtoOutput
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.time.format.DateTimeFormatter


class HolidayListAdapter internal constructor(private var context: Context) : RecyclerView.Adapter<HolidayListAdapter.HolidayViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var posts = emptyList<PostDtoOutput>() // Cached copy of words
    private val formater = DateTimeFormatter.ofPattern("dd-MM-yyyy")


    inner class HolidayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleItemView: TextView = itemView.findViewById(R.id.Title)
        val textItemView: TextView = itemView.findViewById(R.id.TextContnet)
        val pagerView: ViewPager = itemView.findViewById(R.id.PagerView)
        val dateItemView : TextView = itemView.findViewById(R.id.dateText)
        val localItemView : TextView = itemView.findViewById(R.id.localistaionText)
        var indicator : ScrollingPagerIndicator = itemView.findViewById(R.id.indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return HolidayViewHolder(itemView)
    }

    //gdzie indziej pagerAdapter
    override fun onBindViewHolder(holder: HolidayViewHolder, position: Int) {
        val current = posts[position]
        holder.titleItemView.text = current.title
        holder.textItemView.text = current.text
        holder.dateItemView.text = current.date.format(formater)
        holder.localItemView.text = current.location.toString() //to do na miejscowość i kraj
        val adapter = ViewPagerAdapter(context, current.uriList)
        holder.pagerView.adapter = adapter
        if(current.uriList.count() > 1){
            holder.indicator.attachToPager(holder.pagerView)
        }
    }


    internal fun setPosts(posts: List<PostDtoOutput>) {
        this.posts = posts
    }


    override fun getItemCount() = posts.size
}