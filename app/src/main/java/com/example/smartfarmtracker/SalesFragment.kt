package com.example.smartfarmtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class SalesFragment : Fragment() {

    companion object {
        private const val ARG_ANIMAL_ID = "animalId"

        fun newInstance(animalId: String) = SalesFragment().apply {
            arguments = Bundle().apply { putString(ARG_ANIMAL_ID, animalId) }
        }
    }

    private var animalId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animalId = arguments?.getString(ARG_ANIMAL_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sales, container, false)
}
