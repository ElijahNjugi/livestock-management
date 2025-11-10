package com.example.smartfarmtracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
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

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: AnimalAdapter
    private var animalsList = mutableListOf<Animal>()
    private var listener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_animals)

        // Initialize views
        recyclerView = findViewById(R.id.rvAnimals)
        fabAddAnimal = findViewById(R.id.btnAddAnimal)
        tvTotalAnimals = findViewById(R.id.tvManageAnimalsTitle)
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage)

        // Initialize Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Setup RecyclerView
        adapter = AnimalAdapter(animalsList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Floating action button - add new animal
        fabAddAnimal.setOnClickListener {
            val intent = Intent(this, AddAnimalActivity::class.java)
            startActivity(intent)
        }

        // Load data
        fetchAnimalsFromFirestore()
    }

    private fun fetchAnimalsFromFirestore() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_LONG).show()
            return
        }

        // Real-time updates from Firestore
        listener = db.collection("users")
            .document(userId)
            .collection("animals")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    animalsList.clear()
                    for (doc in snapshot.documents) {
                        val animal = doc.toObject(Animal::class.java)
                        if (animal != null) animalsList.add(animal)
                    }

                    adapter.updateData(animalsList)
                    tvTotalAnimals.text = "Manage Animals (${animalsList.size})"
                    tvEmptyMessage.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                } else {
                    // No animals found
                    animalsList.clear()
                    adapter.updateData(animalsList)
                    recyclerView.visibility = View.GONE
                    tvEmptyMessage.visibility = View.VISIBLE
                    tvTotalAnimals.text = "Manage Animals (0)"
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove listener when activity is destroyed
        listener?.remove()
    }
}
