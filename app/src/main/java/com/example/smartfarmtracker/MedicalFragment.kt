package com.example.smartfarmtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.smartfarmtracker.R

class MedicalFragment : Fragment(R.layout.fragment_medical) {

    companion object {
        fun newInstance(animalId: String) = MedicalFragment().apply {
            arguments = Bundle().apply {
                putString("animal_id", animalId)
            }
        }
    }
}
