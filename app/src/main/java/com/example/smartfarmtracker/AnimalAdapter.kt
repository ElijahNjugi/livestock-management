package com.example.smartfarmtracker

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartfarmtracker.model.Animal

class AnimalAdapter(private var animals: List<Animal>) :
    RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textAnimalName: TextView = itemView.findViewById(R.id.textAnimalName)
        val textAnimalType: TextView = itemView.findViewById(R.id.textAnimalType)
        val textAnimalAge: TextView = itemView.findViewById(R.id.textAnimalAge)
        val textAnimalHealth: TextView = itemView.findViewById(R.id.textAnimalHealth)
        val textAnimalWeight: TextView = itemView.findViewById(R.id.textAnimalWeight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animals[position]
        holder.textAnimalName.text = animal.name.ifEmpty { "Unnamed Animal" }
        holder.textAnimalType.text = "Type: ${animal.type.ifEmpty { "N/A" }}"
        holder.textAnimalAge.text = "Age: ${animal.age} months"
        holder.textAnimalHealth.text = "Health: ${animal.healthStatus.ifEmpty { "Unknown" }}"
        holder.textAnimalWeight.text = "Weight: ${animal.weightKg} kg"

        // Navigate to detail page
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, AnimalDetailActivity::class.java)
            intent.putExtra("animalId", animal.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = animals.size

    fun updateData(newList: List<Animal>) {
        animals = newList
        notifyDataSetChanged()
    }
}
