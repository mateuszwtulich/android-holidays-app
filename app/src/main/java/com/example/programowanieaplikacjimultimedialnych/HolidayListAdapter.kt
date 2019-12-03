package com.example.programowanieaplikacjimultimedialnych

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.programowanieaplikacjimultimedialnych.DataBase.PostDto

class HolidayListAdapter internal constructor(context: Context) : RecyclerView.Adapter<HolidayListAdapter.HolidayViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var posts = emptyList<PostDto>() // Cached copy of words


    inner class HolidayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleItemView: TextView = itemView.findViewById(R.id.Title)
        val textItemView: TextView = itemView.findViewById(R.id.TextContnet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return HolidayViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: HolidayViewHolder, position: Int) {
        val current = posts[position]
        holder.titleItemView.text = current.title
        holder.textItemView.text = current.text

    }

    internal fun setPosts(posts: List<PostDto>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    override fun getItemCount() = posts.size
}