package com.example.smartfarmtracker

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.smartfarmtracker.model.SalesEntry
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddSalesDialog : DialogFragment() {

    companion object {
        private const val ARG_ANIMAL_ID = "animalId"
        private const val ARG_ANIMAL_TYPE = "animalType"

        fun newInstance(animalId: String, animalType: String): AddSalesDialog {
            val fragment = AddSalesDialog()
            val bundle = Bundle()
            bundle.putString(ARG_ANIMAL_ID, animalId)
            bundle.putString(ARG_ANIMAL_TYPE, animalType)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var animalId: String
    private lateinit var animalType: String

    private lateinit var edtDate: EditText
    private lateinit var spinnerProduce: Spinner
    private lateinit var edtQuantity: EditText
    private lateinit var edtUnitPrice: EditText
    private lateinit var edtNotes: EditText
    private lateinit var btnSave: Button

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animalId = arguments?.getString(ARG_ANIMAL_ID) ?: ""
        animalType = arguments?.getString(ARG_ANIMAL_TYPE) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.dialog_add_sales, container, false)

        edtDate = v.findViewById(R.id.edtSaleDate)
        spinnerProduce = v.findViewById(R.id.spinnerProduceType)
        edtQuantity = v.findViewById(R.id.edtQuantity)
        edtUnitPrice = v.findViewById(R.id.edtUnitPrice)
        edtNotes = v.findViewById(R.id.edtNotes)
        btnSave = v.findViewById(R.id.btnSaveSale)

        setupSpinner()
        setupDatePicker()

        btnSave.setOnClickListener { saveSale() }

        return v
    }

    private fun setupSpinner() {
        val items = listOf("Milk", "Eggs", "Wool", "Meat", "Other")
        spinnerProduce.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
    }

    private fun setupDatePicker() {
        edtDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    edtDate.setText("$day/${month + 1}/$year")
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun saveSale() {
        val dateStr = edtDate.text.toString().trim()
        val produce = spinnerProduce.selectedItem.toString()
        val quantity = edtQuantity.text.toString().toDoubleOrNull() ?: 0.0
        val unitPrice = edtUnitPrice.text.toString().toDoubleOrNull() ?: 0.0
        val notes = edtNotes.text.toString().trim()

        if (dateStr.isEmpty() || quantity <= 0 || unitPrice <= 0) {
            Toast.makeText(requireContext(), "Please fill all required fields", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val parts = dateStr.split("/")
        val cal = Calendar.getInstance()
        cal.set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt(), 0, 0)
        val ts = Timestamp(cal.time)

        val uid = auth.currentUser?.uid ?: return
        val saleId = db.collection("users").document(uid)
            .collection("sales")
            .document().id

        val total = quantity * unitPrice
        val entry = SalesEntry(
            id = saleId,
            dateOfSale = ts,
            animalId = animalId,
            animalType = animalType,
            produceType = produce,
            quantity = quantity,
            unitPrice = unitPrice,
            totalAmount = total,
            notes = notes,
            createdAt = System.currentTimeMillis()
        )

        db.collection("users").document(uid)
            .collection("sales")
            .document(saleId)
            .set(entry)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Sale saved!", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to save sale", Toast.LENGTH_SHORT).show()
            }
    }
}
