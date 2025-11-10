package com.example.smartfarmtracker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AnimalDetailActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_detail)

        val animalId = intent.getStringExtra("animalId")
        db = FirebaseFirestore.getInstance()

        val textName = findViewById<TextView>(R.id.textName)
        val textType = findViewById<TextView>(R.id.textType)
        val textAge = findViewById<TextView>(R.id.textAge)
        val textHealth = findViewById<TextView>(R.id.textHealth)

        if (animalId != null) {
            db.collection("animals").document(animalId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        textName.text = "Name: ${document.getString("name")}"
                        textType.text = "Type: ${document.getString("type")}"
                        textAge.text = "Age: ${document.getString("age")}"
                        textHealth.text = "Health: ${document.getString("healthStatus")}"
                    }
                }
                .addOnFailureListener {
                    textName.text = "Failed to load animal data."
                }
        }
    }
}
