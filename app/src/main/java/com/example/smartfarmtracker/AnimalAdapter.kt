package com.example.smartfarmtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartfarmtracker.model.Animal

class AnimalAdapter(
    private val animalList: List<Animal>,
    private val onItemClick: (Animal) -> Unit
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    inner class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.findViewById(R.id.itemAnimalId)
        val tvType: TextView = itemView.findViewById(R.id.itemAnimalType)
        val tvWeight: TextView = itemView.findViewById(R.id.itemAnimalWeight)
        val tvLastCheckup: TextView = itemView.findViewById(R.id.itemAnimalLastCheckup)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animalList[position]
        holder.tvId.text = "ID: ${animal.id}"
        holder.tvType.text = "Type: ${animal.type}"
        holder.tvWeight.text = "Weight: ${animal.weight}"
        holder.tvLastCheckup.text = "Last Checkup: ${animal.lastCheckup}"

        holder.itemView.setOnClickListener { onItemClick(animal) }
    }

    override fun getItemCount(): Int = animalList.size
}
