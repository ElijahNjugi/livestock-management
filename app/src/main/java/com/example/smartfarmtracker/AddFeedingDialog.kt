package com.example.smartfarmtracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import java.util.*

class AddFeedingDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_feeding, container, false)

        val etDate = view.findViewById<EditText>(R.id.etFeedingDate)
        val etType = view.findViewById<EditText>(R.id.etFeedingType)
        val etQuantity = view.findViewById<EditText>(R.id.etFeedingQty)
        val spinnerUnit = view.findViewById<Spinner>(R.id.spinnerUnit)
        val btnSave = view.findViewById<Button>(R.id.btnSaveFeeding)

        val units = arrayOf("kg", "grams", "liters")
        spinnerUnit.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, units)

        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(),
                { _, year, month, dayOfMonth ->
                    etDate.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnSave.setOnClickListener {
            Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        return view
    }
}
