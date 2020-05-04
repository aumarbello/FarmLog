package com.aumarbello.farmlog.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aumarbello.farmlog.R
import com.aumarbello.farmlog.models.FarmLocation
import com.google.android.material.chip.Chip

class CoordinatesAdapter (private val removeItemCallback: (FarmLocation) -> Unit): ListAdapter<FarmLocation, CoordinatesAdapter.CoordinatesHolder>(DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoordinatesHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_coordinate, parent, false)

        return CoordinatesHolder(view)
    }

    override fun onBindViewHolder(holder: CoordinatesHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    inner class CoordinatesHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindItem(location: FarmLocation) {
            with(itemView as Chip) {
                text = resources.getString(R.string.format_location, location.latitude, location.longitude)
                setOnCloseIconClickListener {
                    removeItemCallback(location)
                }
            }
        }
    }

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<FarmLocation>() {
            override fun areItemsTheSame(oldItem: FarmLocation, newItem: FarmLocation): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FarmLocation, newItem: FarmLocation): Boolean {
                return oldItem == newItem
            }
        }
    }
}