package com.example.smartfarmtracker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.smartfarmtracker.R

class AddSalesDialog : DialogFragment() {

    private lateinit var etSaleQty: EditText
    private lateinit var etPricePerUnit: EditText
    private lateinit var spinnerItem: Spinner
    private lateinit var spinnerSaleUnit: Spinner
    private lateinit var tvTotalAmount: TextView
    private lateinit var btnSaveSale: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sales, container, false)

        // Bind views
        etSaleQty = view.findViewById(R.id.etSaleQty)
        etPricePerUnit = view.findViewById(R.id.etPricePerUnit)
        spinnerItem = view.findViewById(R.id.spinnerItem)
        spinnerSaleUnit = view.findViewById(R.id.spinnerSaleUnit)
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount)
        btnSaveSale = view.findViewById(R.id.btnSaveSale)

        val items = listOf("Milk", "Meat", "Eggs", "Other")
        spinnerItem.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)

        val units = listOf("kg", "g", "L", "ml")
        spinnerSaleUnit.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, units)

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        etSaleQty.addTextChangedListener(textWatcher)
        etPricePerUnit.addTextChangedListener(textWatcher)

        btnSaveSale.setOnClickListener {
            val quantity = etSaleQty.text.toString().toDoubleOrNull() ?: 0.0
            val price = etPricePerUnit.text.toString().toDoubleOrNull() ?: 0.0
            val item = spinnerItem.selectedItem.toString()
            val unit = spinnerSaleUnit.selectedItem.toString()
            val total = quantity * price

            Toast.makeText(requireContext(), "Saved: $item, $quantity $unit, Total: $total", Toast.LENGTH_SHORT).show()

            dismiss()
        }

        return view
    }

    private fun calculateTotal() {
        val quantity = etSaleQty.text.toString().toDoubleOrNull() ?: 0.0
        val price = etPricePerUnit.text.toString().toDoubleOrNull() ?: 0.0
        val total = quantity * price
        tvTotalAmount.text = "Total: $total"
    }
}
