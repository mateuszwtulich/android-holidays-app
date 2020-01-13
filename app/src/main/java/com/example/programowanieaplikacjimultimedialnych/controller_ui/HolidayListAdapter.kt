package com.example.programowanieaplikacjimultimedialnych.controller_ui

import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.programowanieaplikacjimultimedialnych.view_model.dto.PostDtoOutput
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.time.format.DateTimeFormatter
import com.example.programowanieaplikacjimultimedialnych.R



class HolidayListAdapter internal constructor(private var context: Context, private val onPostListner: OnPostListner) : RecyclerView.Adapter<HolidayListAdapter.HolidayViewHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val formater = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    private var postsList = emptyList<PostDtoOutput>()
    private var postListFiltered = emptyList<PostDtoOutput>()
    private val geocoder = Geocoder(context)

    interface OnPostListner{
        fun onPostClick(position: Int, image:Int)
    }

    inner class HolidayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleItemView: TextView = itemView.findViewById(R.id.Title)

        val pagerView: ViewPager = itemView.findViewById(R.id.PagerView)
        val dateItemView : TextView = itemView.findViewById(R.id.dateText)
        val localItemView : TextView = itemView.findViewById(R.id.localization)
        val indicator : ScrollingPagerIndicator = itemView.findViewById(R.id.indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return HolidayViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HolidayViewHolder, position: Int) {

        val current = postListFiltered[position]
        val adapter = ViewPagerAdapter(context, current.uriList,onPostListner,position)

        holder.titleItemView.text = current.title
        holder.dateItemView.text = current.date.format(formater)

        val adresses = geocoder.getFromLocation(current.location.latitude, current.location.longitude, 1)[0]
        holder.localItemView.text = adresses.locality.toString()
        holder.pagerView.adapter = adapter
        holder.pagerView.offscreenPageLimit = 6


        holder.itemView.setOnClickListener {
            onPostListner.onPostClick(position,holder.pagerView.currentItem)
            Log.d("List postion:" , position.toString())
            Log.d("Image tag:" , "image${holder.pagerView.currentItem}")
        }

        if(current.uriList.count() > 1)
            holder.indicator.visibility = View.VISIBLE
        else
            holder.indicator.visibility = View.INVISIBLE

        holder.indicator.attachToPager(holder.pagerView)

    }

    internal fun setPosts(posts: List<PostDtoOutput>) {
        this.postsList = posts.reversed()
        this.postListFiltered = posts.reversed()
        notifyDataSetChanged()
    }

    override fun getItemCount() = postListFiltered.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString().toLowerCase()
                if (charString.isEmpty()) {
                    postListFiltered = postsList
                }
                else {
                    val filteredList = mutableListOf<PostDtoOutput>()
                    for (row in postsList) {
                        if (row.text.toLowerCase().contains(charString) ||
                            row.title.toLowerCase().contains(charString) ||
                            row.date.toString().contains(charString) ||
                            row.location.toString().contains(charString))
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
                if(results?.values is ArrayList<*>){
                    postListFiltered = results.values as ArrayList<PostDtoOutput>
                    notifyDataSetChanged()
                }
            }
        }
    }

    fun getFilterdPost(position: Int):PostDtoOutput{
        return postListFiltered[position]
    }

    override fun getItemId(position: Int): Long {
        return postListFiltered[position].id.hashCode().toLong()
    }
}

