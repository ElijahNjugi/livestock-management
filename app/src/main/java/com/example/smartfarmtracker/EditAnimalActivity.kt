package com.example.smartfarmtracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var animalId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_animal)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        etId = findViewById(R.id.etAnimalId)
        etType = findViewById(R.id.etAnimalType)
        etWeight = findViewById(R.id.etAnimalWeight)
        etLastCheckup = findViewById(R.id.etAnimalLastCheckup)
        btnSave = findViewById(R.id.btnSaveAnimal)

        animalId = intent.getStringExtra("animalId") ?: ""

        btnSave.setOnClickListener {
            saveAnimal()
        }
    }

    private fun saveAnimal() {
        val uid = auth.currentUser?.uid ?: return

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
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update animal", Toast.LENGTH_SHORT).show()
            }
    }
}
