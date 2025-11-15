package com.example.smartfarmtracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import java.util.*

class AddMedicalDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_add_medical, container, false)

        val etDate = view.findViewById<EditText>(R.id.etMedicalDate)
        val etType = view.findViewById<EditText>(R.id.etMedicalType)
        val etMedication = view.findViewById<EditText>(R.id.etMedication)
        val etDosage = view.findViewById<EditText>(R.id.etDosage)
        val etVet = view.findViewById<EditText>(R.id.etVeterinarian)
        val btnSave = view.findViewById<Button>(R.id.btnSaveMedical)

        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                etDate.setText(String.format("%04d-%02d-%02d", y, m + 1, d))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnSave.setOnClickListener {
            Toast.makeText(requireContext(), "Medical record saved!", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        return view
    }
}
