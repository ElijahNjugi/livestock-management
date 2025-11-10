package com.example.smartfarmtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.smartfarmtracker.R

class NotesFragment : Fragment(R.layout.fragment_notes) {

    companion object {
        fun newInstance(animalId: String) = NotesFragment().apply {
            arguments = Bundle().apply {
                putString("animal_id", animalId)
            }
        }
    }
}
