package com.example.smartfarmtracker.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.smartfarmtracker.R

class FeedingFragment : Fragment(R.layout.fragment_feeding) {

    companion object {
        fun newInstance(animalId: String) = FeedingFragment().apply {
            arguments = Bundle().apply {
                putString("animal_id", animalId)
            }
        }
    }
}
