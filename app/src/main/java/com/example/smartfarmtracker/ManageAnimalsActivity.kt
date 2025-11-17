package com.example.smartfarmtracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartfarmtracker.model.Animal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ManageAnimalsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var layoutAnimalTypes: LinearLayout
    private lateinit var btnCow: Button
    private lateinit var btnGoat: Button
    private lateinit var btnSheep: Button
    private lateinit var btnPig: Button
    private lateinit var btnPoultry: Button

    private lateinit var btnAddAnimal: ImageButton

    private var selectedType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_animals)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Initialize views
        layoutAnimalTypes = findViewById(R.id.layoutAnimalTypes)
        btnCow = findViewById(R.id.btnCow)
        btnGoat = findViewById(R.id.btnGoat)
        btnSheep = findViewById(R.id.btnSheep)
        btnPig = findViewById(R.id.btnPig)
        btnPoultry = findViewById(R.id.btnPoultry)
        btnAddAnimal = findViewById(R.id.btnAddAnimal)

        // Animal type button click listener
        val typeClickListener = View.OnClickListener { view ->
            selectedType = when (view.id) {
                R.id.btnCow -> "Cow"
                R.id.btnGoat -> "Goat"
                R.id.btnSheep -> "Sheep"
                R.id.btnPig -> "Pig"
                R.id.btnPoultry -> "Poultry"
                else -> ""
            }

            if (selectedType.isNotEmpty()) {
                // Open AnimalListActivity with the selected type
                val intent = Intent(this, AnimalListActivity::class.java)
                intent.putExtra("animalType", selectedType)
                startActivity(intent)
            }
        }

        btnCow.setOnClickListener(typeClickListener)
        btnGoat.setOnClickListener(typeClickListener)
        btnSheep.setOnClickListener(typeClickListener)
        btnPig.setOnClickListener(typeClickListener)
        btnPoultry.setOnClickListener(typeClickListener)

        // Add new animal button
        btnAddAnimal.setOnClickListener {
            if (selectedType.isEmpty()) {
                Toast.makeText(this, "Please select an animal type first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, AddAnimalActivity::class.java)
            intent.putExtra("animalType", selectedType)
            startActivity(intent)
        }
    }
}
