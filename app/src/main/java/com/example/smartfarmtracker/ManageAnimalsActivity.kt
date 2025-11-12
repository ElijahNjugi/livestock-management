package com.example.smartfarmtracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartfarmtracker.model.Animal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ManageAnimalsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddAnimal: ImageButton
    private lateinit var tvTotalAnimals: TextView
    private lateinit var tvEmptyMessage: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: AnimalAdapter
    private var animalsList = mutableListOf<Animal>()
    private var listener: ListenerRegistration? = null
    private var isActivityActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_animals)

        // Initialize views
        recyclerView = findViewById(R.id.rvAnimals)
        fabAddAnimal = findViewById(R.id.btnAddAnimal)
        tvTotalAnimals = findViewById(R.id.tvManageAnimalsTitle)
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage)
        progressBar = findViewById(R.id.progressBar) // Add this in your layout (optional)

        // Initialize Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Setup RecyclerView
        adapter = AnimalAdapter(animalsList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Add new animal button
        fabAddAnimal.setOnClickListener {
            val intent = Intent(this, AddAnimalActivity::class.java)
            startActivity(intent)
        }

        isActivityActive = true
        fetchAnimalsFromFirestore()
    }

    private fun fetchAnimalsFromFirestore() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_LONG).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        listener = db.collection("users")
            .document(userId)
            .collection("animals")
            .addSnapshotListener { snapshot, error ->
                if (!isActivityActive) return@addSnapshotListener // Prevent updates after destroy

                progressBar.visibility = View.GONE

                if (error != null) {
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                if (snapshot == null || snapshot.isEmpty) {
                    animalsList.clear()
                    adapter.updateData(animalsList)
                    recyclerView.visibility = View.GONE
                    tvEmptyMessage.visibility = View.VISIBLE
                    tvTotalAnimals.text = "Manage Animals (0)"
                } else {
                    animalsList.clear()
                    for (doc in snapshot.documents) {
                        doc.toObject(Animal::class.java)?.let { animalsList.add(it) }
                    }

                    adapter.updateData(animalsList)
                    recyclerView.visibility = View.VISIBLE
                    tvEmptyMessage.visibility = View.GONE
                    tvTotalAnimals.text = "Manage Animals (${animalsList.size})"
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        isActivityActive = false
        listener?.remove()
    }
}
