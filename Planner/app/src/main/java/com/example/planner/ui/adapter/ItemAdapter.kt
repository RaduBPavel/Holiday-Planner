package com.example.planner.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planner.R
import com.example.planner.ui.locations.Location

class ItemAdapter(private val context: Context, var dataset: List<Location>)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.item_title)
        val temp: TextView = view.findViewById(R.id.item_temp)
        val humidity: TextView = view.findViewById(R.id.item_humidity)
        val image: ImageView = view.findViewById(R.id.city_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.new_list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.title.text = item.name
        holder.temp.text = "Temperature (C): " + item.temperature.toString()
        holder.humidity.text = "Humidity: " + item.humidity.toString()
        if (item.isDay) {
            holder.image.setImageResource(R.drawable.day_image)
        } else {
            holder.image.setImageResource(R.drawable.night_image)
        }
    }

    fun modifyItem(position: Int) {
        notifyItemChanged(position)
    }

    override fun getItemCount() = dataset.size
}