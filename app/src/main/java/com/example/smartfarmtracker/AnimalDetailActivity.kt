package com.example.smartfarmtracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.smartfarmtracker.model.Animal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class AnimalDetailActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var animalId: String
    private var listener: ListenerRegistration? = null

    private lateinit var idText: TextView
    private lateinit var typeText: TextView
    private lateinit var weightText: TextView
    private lateinit var lastCheckupText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var salesButton: Button

    private var animalType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_detail)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        idText = findViewById(R.id.animalIdText)
        typeText = findViewById(R.id.animalTypeText)
        weightText = findViewById(R.id.animalWeightText)
        lastCheckupText = findViewById(R.id.animalLastCheckupText)
        progressBar = findViewById(R.id.progressBar)
        editButton = findViewById(R.id.editButton)
        deleteButton = findViewById(R.id.deleteButton)
        salesButton = findViewById(R.id.btnSales)

        animalId = intent.getStringExtra("animalId") ?: ""
        val userId = auth.currentUser?.uid

        if (userId.isNullOrEmpty() || animalId.isEmpty()) {
            Toast.makeText(this, "Invalid user or animal ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchAnimalDetails(userId, animalId)

        // Edit button - open EditAnimalActivity
        editButton.setOnClickListener {
            val intent = Intent(this, EditAnimalActivity::class.java)
            intent.putExtra("animalId", animalId)
            startActivity(intent)
        }

        // Delete button - confirm and delete
        deleteButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Animal")
                .setMessage("Are you sure you want to delete this animal?")
                .setPositiveButton("Yes") { _, _ ->
                    val uid = auth.currentUser?.uid
                    if (uid.isNullOrEmpty()) {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    db.collection("users").document(uid)
                        .collection("animals").document(animalId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Animal deleted", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to delete: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        // Sales button
        salesButton.setOnClickListener {
            if (animalType.isEmpty()) {
                Toast.makeText(this, "Animal type not loaded yet", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            AddSalesDialog.newInstance(animalId, animalType)
                .show(supportFragmentManager, "AddSale")
        }
    }

    override fun onResume() {
        super.onResume()
        // Auto-refresh when returning from EditAnimalActivity
        val uid = auth.currentUser?.uid
        if (!uid.isNullOrEmpty() && animalId.isNotEmpty()) {
            fetchAnimalDetails(uid, animalId)
        }
    }

    private fun fetchAnimalDetails(userId: String, animalId: String) {
        progressBar.visibility = View.VISIBLE

        listener?.remove() // remove old listener if any
        listener = db.collection("users").document(userId)
            .collection("animals").document(animalId)
            .addSnapshotListener { snapshot, error ->
                progressBar.visibility = View.GONE
                if (error != null) {
                    Toast.makeText(this, "Failed to load animal details", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val animal = snapshot.toObject(Animal::class.java)
                    if (animal != null) {
                        populateAnimalDetails(animal)
                        animalType = animal.type
                    }
                } else {
                    Toast.makeText(this, "Animal not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
    }

    private fun populateAnimalDetails(animal: Animal) {
        idText.text = "Animal ID: ${animal.id}"
        typeText.text = "Type: ${animal.type}"
        weightText.text = "Weight: ${animal.weight} kg"
        lastCheckupText.text = "Last Checkup: ${animal.lastCheckup.ifEmpty { "N/A" }}"
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.remove()
    }
}
