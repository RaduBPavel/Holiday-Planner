package com.example.planner.ui.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.planner.R

class LocationFragment : Fragment() {

    companion object {
        fun newInstance() = LocationFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // receiving the data passed from activity here
        val view = inflater.inflate(R.layout.fragment_location, container, false)

        val text: TextView = view.findViewById(R.id.item_title)
        text.text = requireArguments().get("item_title").toString()

        val temp: TextView = view.findViewById(R.id.item_temp)
        temp.text = requireArguments().get("item_temp").toString()

        val humidity: TextView = view.findViewById(R.id.item_humidity)
        humidity.text = requireArguments().get("item_humidity").toString()

        val image: ImageView = view.findViewById(R.id.city_image)
        if (requireArguments().get("is_day") == 0) {
            image.setImageResource(R.drawable.day_image)
        } else {
            image.setImageResource(R.drawable.night_image)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}