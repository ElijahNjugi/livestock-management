package com.example.smartfarmtracker.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartfarmtracker.R
import com.example.smartfarmtracker.model.Feeding

class FeedingAdapter(private val items: List<Feeding>) :
    RecyclerView.Adapter<FeedingAdapter.FeedingViewHolder>() {

    class FeedingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateText: TextView = view.findViewById(R.id.feedDate)
        val typeText: TextView = view.findViewById(R.id.feedType)
        val qtyText: TextView = view.findViewById(R.id.feedQty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feeding, parent, false)
        return FeedingViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FeedingViewHolder, position: Int) {
        val item = items[position]
        holder.dateText.text = item.date
        holder.typeText.text = item.foodType
        holder.qtyText.text = "${item.quantity} kg"
    }
}
