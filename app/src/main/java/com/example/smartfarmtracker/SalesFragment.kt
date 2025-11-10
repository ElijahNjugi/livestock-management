package com.example.smartfarmtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.smartfarmtracker.R

class SalesFragment : Fragment(R.layout.fragment_sales) {

    companion object {
        fun newInstance(animalId: String) = SalesFragment().apply {
            arguments = Bundle().apply {
                putString("animal_id", animalId)
            }
        }
    }
}
