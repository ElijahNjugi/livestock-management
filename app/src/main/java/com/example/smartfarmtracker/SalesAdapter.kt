package com.example.smartfarmtracker.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartfarmtracker.R
import com.example.smartfarmtracker.model.Sale

class SalesAdapter(private val items: List<Sale>) :
    RecyclerView.Adapter<SalesAdapter.SalesViewHolder>() {

    class SalesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateText: TextView = view.findViewById(R.id.saleDate)
        val itemText: TextView = view.findViewById(R.id.saleItem)
        val amountText: TextView = view.findViewById(R.id.saleAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sale, parent, false)
        return SalesViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SalesViewHolder, position: Int) {
        val item = items[position]
        holder.dateText.text = item.date
        holder.itemText.text = item.itemName
        holder.amountText.text = "$${item.amount}"
    }
}
