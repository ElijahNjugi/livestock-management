package com.example.smartfarmtracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartfarmtracker.model.SalesEntry
import com.example.smartfarmtracker.ui.adapters.SalesAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class SalesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SalesAdapter
    private val salesList = mutableListOf<SalesEntry>()

    private lateinit var fabAddSale: FloatingActionButton
    private lateinit var btnToday: Button
    private lateinit var btnWeek: Button
    private lateinit var btnMonth: Button
    private lateinit var btnYear: Button

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales)

        recyclerView = findViewById(R.id.rvSales)
        fabAddSale = findViewById(R.id.fabAddSale)
        btnToday = findViewById(R.id.btnToday)
        btnWeek = findViewById(R.id.btnWeek)
        btnMonth = findViewById(R.id.btnMonth)
        btnYear = findViewById(R.id.btnYear)

        adapter = SalesAdapter(salesList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchAllSales()

        fabAddSale.setOnClickListener {
            val intent = Intent(this, ManageAnimalsActivity::class.java)
            intent.putExtra("selectForSale", true)
            startActivity(intent)
        }

        btnToday.setOnClickListener { filterSales("today") }
        btnWeek.setOnClickListener { filterSales("week") }
        btnMonth.setOnClickListener { filterSales("month") }
        btnYear.setOnClickListener { filterSales("year") }
    }

    // Handle back button click in toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun fetchAllSales() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid)
            .collection("sales")
            .orderBy("dateOfSale", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                salesList.clear()
                for (doc in result.documents) {
                    val sale = doc.toObject(SalesEntry::class.java)
                    if (sale != null) {
                        sale.id = doc.id
                        salesList.add(sale)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch sales", Toast.LENGTH_SHORT).show()
            }
    }

    private fun filterSales(period: String) {
        val now = Calendar.getInstance()
        val filtered = salesList.filter { sale ->
            val saleCal = Calendar.getInstance().apply { sale.dateOfSale?.let { time = it.toDate() } }

            when (period) {
                "today" -> saleCal.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                        && saleCal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
                "week" -> saleCal.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                        && saleCal.get(Calendar.WEEK_OF_YEAR) == now.get(Calendar.WEEK_OF_YEAR)
                "month" -> saleCal.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                        && saleCal.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                "year" -> saleCal.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                else -> true
            }
        }
        adapter.updateList(filtered)
    }
}
