package com.example.smartfarmtracker.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartfarmtracker.R
import com.example.smartfarmtracker.model.MedicalRecord

class MedicalAdapter(private val items: List<MedicalRecord>) :
    RecyclerView.Adapter<MedicalAdapter.MedicalViewHolder>() {

    class MedicalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateText: TextView = view.findViewById(R.id.medDate)
        val treatmentText: TextView = view.findViewById(R.id.medTreatment)
        val notesText: TextView = view.findViewById(R.id.medNotes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medical, parent, false)
        return MedicalViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MedicalViewHolder, position: Int) {
        val item = items[position]
        holder.dateText.text = item.date
        holder.treatmentText.text = item.treatment
        holder.notesText.text = item.notes
    }
}
