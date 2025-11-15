package com.example.smartfarmtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SalesFragment : Fragment() {
    companion object {
        private const val ARG_ANIMAL_ID = "animalId"
        fun newInstance(animalId: String) = SalesFragment().apply {
            arguments = Bundle().apply { putString(ARG_ANIMAL_ID, animalId) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sales, container, false)
        val fabAdd: FloatingActionButton = view.findViewById(R.id.fabAddSales)
        fabAdd.setOnClickListener {
            AddSalesDialog().show(childFragmentManager, "AddSales")
        }
        return view
    }
}
