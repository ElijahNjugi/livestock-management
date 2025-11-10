package com.example.smartfarmtracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartfarmtracker.model.Animal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddAnimalActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_animal)

        db = FirebaseFirestore.getInstance()

        val etId = findViewById<EditText>(R.id.etAnimalId)
        val etType = findViewById<EditText>(R.id.etAnimalType)
        val etName = findViewById<EditText>(R.id.etAnimalName)
        val etAge = findViewById<EditText>(R.id.etAnimalAge)
        val etHealth = findViewById<EditText>(R.id.etAnimalHealth)
        val etWeight = findViewById<EditText>(R.id.etAnimalWeight)
        val etLastFeed = findViewById<EditText>(R.id.etLastFeedDate)
        val etLastCheckup = findViewById<EditText>(R.id.etLastCheckupDate)
        val btnSave = findViewById<Button>(R.id.btnSaveAnimal)

        btnSave.setOnClickListener {
            val id = etId.text.toString().trim()
            val type = etType.text.toString().trim()

            if (id.isEmpty() || type.isEmpty()) {
                Toast.makeText(this, "Animal ID and Type are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val name = etName.text.toString().trim()
            val age = etAge.text.toString().toIntOrNull() ?: 0
            val health = etHealth.text.toString().trim()
            val weight = etWeight.text.toString().toFloatOrNull() ?: 0f
            val lastFeed = etLastFeed.text.toString().trim()
            val lastCheckup = etLastCheckup.text.toString().trim()

            val animal = Animal(
                id = id,
                type = type,
                name = name,
                age = age,
                healthStatus = health,
                weightKg = weight,
                lastFeedDate = lastFeed,
                lastCheckupDate = lastCheckup
            )

            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener

            // Save under user's collection
            db.collection("users")
                .document(userId)
                .collection("animals")
                .document(animal.id)
                .set(animal)
                .addOnSuccessListener {
                    Toast.makeText(this, "Animal saved successfully!", Toast.LENGTH_SHORT).show()
                    finish() // go back to ManageAnimalsActivity
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving animal: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
