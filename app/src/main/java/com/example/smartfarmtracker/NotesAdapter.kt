package com.example.smartfarmtracker.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartfarmtracker.R
import com.example.smartfarmtracker.model.Note

class NotesAdapter(private val items: List<Note>) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentText: TextView = view.findViewById(R.id.noteContent)
        val dateText: TextView = view.findViewById(R.id.noteDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val item = items[position]
        holder.contentText.text = item.content
        holder.dateText.text = item.date
    }
}
