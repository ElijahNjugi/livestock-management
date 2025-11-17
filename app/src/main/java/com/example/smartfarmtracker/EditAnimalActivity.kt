package com.example.smartfarmtracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartfarmtracker.model.Animal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditAnimalActivity : AppCompatActivity() {

    private lateinit var etId: EditText
    private lateinit var etType: EditText
    private lateinit var etWeight: EditText
    private lateinit var etLastCheckup: EditText
    private lateinit var btnSave: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var animalId: String
    private var uid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_animal)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid ?: ""

        if (uid.isEmpty()) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        etId = findViewById(R.id.etAnimalId)
        etType = findViewById(R.id.etAnimalType)
        etWeight = findViewById(R.id.etAnimalWeight)
        etLastCheckup = findViewById(R.id.etAnimalLastCheckup)
        btnSave = findViewById(R.id.btnSaveAnimal)
        progressBar = findViewById(R.id.progressBar)

        animalId = intent.getStringExtra("animalId") ?: ""
        if (animalId.isEmpty()) {
            Toast.makeText(this, "Animal ID missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Fetch current animal data and pre-fill form
        fetchAnimalData()

        btnSave.setOnClickListener {
            saveAnimal()
        }
    }

    private fun fetchAnimalData() {
        progressBar.visibility = ProgressBar.VISIBLE

        db.collection("users").document(uid)
            .collection("animals").document(animalId)
            .get()
            .addOnSuccessListener { snapshot ->
                progressBar.visibility = ProgressBar.GONE
                if (snapshot.exists()) {
                    val animal = snapshot.toObject(Animal::class.java)
                    if (animal != null) {
                        etId.setText(animal.id)
                        etType.setText(animal.type)
                        etWeight.setText(animal.weight)
                        etLastCheckup.setText(animal.lastCheckup)
                    }
                } else {
                    Toast.makeText(this, "Animal not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener { e ->
                progressBar.visibility = ProgressBar.GONE
                Toast.makeText(this, "Failed to load animal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveAnimal() {
        val animal = Animal(
            id = etId.text.toString(),
            type = etType.text.toString(),
            weight = etWeight.text.toString(),
            lastCheckup = etLastCheckup.text.toString()
        )

        db.collection("users").document(uid)
            .collection("animals").document(animalId)
            .set(animal)
            .addOnSuccessListener {
                Toast.makeText(this, "Animal updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update animal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
