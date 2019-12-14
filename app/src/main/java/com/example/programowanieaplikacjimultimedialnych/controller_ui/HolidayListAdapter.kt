package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.programowanieaplikacjimultimedialnych.view_model.dto.PostDtoOutput
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.time.format.DateTimeFormatter
import android.widget.Filter
import android.widget.Filterable
import com.example.programowanieaplikacjimultimedialnych.R


class HolidayListAdapter internal constructor(private var context: Context) : RecyclerView.Adapter<HolidayListAdapter.HolidayViewHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val formater = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private var postsList = emptyList<PostDtoOutput>()
    private var postListFiltered = emptyList<PostDtoOutput>()// Cached copy of words


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
        val current = postListFiltered[position]
        holder.titleItemView.text = current.title
        holder.textItemView.text = current.text
        holder.dateItemView.text = current.date.format(formater)
        holder.localItemView.text = current.location.toString() //to do na miejscowość i kraj
        val adapter = ViewPagerAdapter(context, current.uriList)
        holder.pagerView.adapter = adapter

        if(current.uriList.count() > 1){                    //visibility może inaczej ?
            holder.indicator.visibility = View.VISIBLE
            holder.indicator.attachToPager(holder.pagerView)
        }
        else
            holder.indicator.visibility = View.INVISIBLE
    }

    internal fun setPosts(posts: List<PostDtoOutput>) {
        this.postsList = posts
        this.postListFiltered = posts
        notifyDataSetChanged()
    }

    override fun getItemCount() = postListFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    postListFiltered = postsList
                }
                else {
                    val filteredList = mutableListOf<PostDtoOutput>()
                    for (row in postsList) {
                        if (row.text.toLowerCase().contains(charString.toLowerCase()) ||
                            row.title.toLowerCase().contains(charSequence) ||
                            row.date.toString().contains(charString.toLowerCase()) ||
                            row.location.toString().contains(charString.toLowerCase()))
                        {
                            filteredList.add(row)
                        }
                    }
                    postListFiltered = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = postListFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                postListFiltered = results?.values as ArrayList<PostDtoOutput>

                // refresh the list with filtered data
                notifyDataSetChanged()
            }
        }
    }
}

