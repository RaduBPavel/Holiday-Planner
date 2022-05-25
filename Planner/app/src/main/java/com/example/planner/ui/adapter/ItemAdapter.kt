package com.example.planner.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planner.R
import com.example.planner.ui.locations.Location

class ItemAdapter(private val context: Context, private val dataset: List<Location>)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.item_title)
        val temp: TextView = view.findViewById(R.id.item_temp)
        val air_temp: TextView = view.findViewById(R.id.item_air_temp)
        val humidity: TextView = view.findViewById(R.id.item_humidity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.title.text = item.name
        holder.temp.text = item.temperature.toString()
        holder.air_temp.text = item.airTemperature.toString()
        holder.humidity.text = item.humidity.toString()
    }

    override fun getItemCount() = dataset.size
}