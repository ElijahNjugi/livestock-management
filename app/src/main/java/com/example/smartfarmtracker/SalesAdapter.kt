package com.example.smartfarmtracker.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartfarmtracker.R
import com.example.smartfarmtracker.model.SalesEntry
import java.text.SimpleDateFormat
import java.util.*

class SalesAdapter(private val salesList: MutableList<SalesEntry>) :
    RecyclerView.Adapter<SalesAdapter.SalesViewHolder>() {

    inner class SalesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvSaleDate)
        val tvAnimalType: TextView = itemView.findViewById(R.id.tvAnimalType)
        val tvProduce: TextView = itemView.findViewById(R.id.tvProduce)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val tvUnitPrice: TextView = itemView.findViewById(R.id.tvUnitPrice)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sale, parent, false)
        return SalesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalesViewHolder, position: Int) {
        val sale = salesList[position]
        val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        holder.tvDate.text = "Date: ${sale.dateOfSale?.toDate()?.let { sdf.format(it) } ?: "N/A"}"
        holder.tvAnimalType.text = "Animal: ${sale.animalType}"
        holder.tvProduce.text = "Produce: ${sale.produceType}"
        holder.tvQuantity.text = "Qty: ${sale.quantity}"
        holder.tvUnitPrice.text = "Unit Price: ${sale.unitPrice}"
        holder.tvTotal.text = "Total: ${sale.totalAmount}"
    }

    override fun getItemCount(): Int = salesList.size

    fun updateList(newList: List<SalesEntry>) {
        salesList.clear()
        salesList.addAll(newList)
        notifyDataSetChanged()
    }
}
