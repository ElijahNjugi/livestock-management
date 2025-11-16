package com.example.smartfarmtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartfarmtracker.model.Animal

class AnimalAdapter(
    private val animals: List<Animal>,
    private val onItemClick: (Animal) -> Unit
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    inner class AnimalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idText: TextView = view.findViewById(R.id.itemAnimalId)
        val typeText: TextView = view.findViewById(R.id.itemAnimalType)
        val weightText: TextView = view.findViewById(R.id.itemAnimalWeight)
        val lastCheckupText: TextView = view.findViewById(R.id.itemAnimalLastCheckup)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animals[position]
        holder.idText.text = "ID: ${animal.id}"
        holder.typeText.text = "Type: ${animal.type}"
        holder.weightText.text = "Weight: ${animal.weight} kg"
        holder.lastCheckupText.text = "Last Checkup: ${animal.lastCheckup.ifEmpty { "N/A" }}"

        holder.itemView.setOnClickListener { onItemClick(animal) }
    }

    override fun getItemCount(): Int = animals.size
}
