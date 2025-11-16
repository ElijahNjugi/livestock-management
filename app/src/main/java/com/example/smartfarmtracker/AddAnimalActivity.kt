package com.example.smartfarmtracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddAnimalActivity : AppCompatActivity() {

    private lateinit var etAnimalId: EditText
    private lateinit var spType: Spinner
    private lateinit var etWeight: EditText
    private lateinit var etLastCheckup: EditText
    private lateinit var btnAdd: Button

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var animalTypeFromIntent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_animal)

        animalTypeFromIntent = intent.getStringExtra("animalType") ?: ""

        etAnimalId = findViewById(R.id.etAnimalId)
        spType = findViewById(R.id.spAnimalType)
        etWeight = findViewById(R.id.etWeight)
        etLastCheckup = findViewById(R.id.etLastCheckup)
        btnAdd = findViewById(R.id.btnAddAnimal)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val types = listOf("Cow", "Goat", "Sheep", "Pig", "Poultry")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spType.adapter = adapter
        spType.setSelection(types.indexOf(animalTypeFromIntent))

        btnAdd.setOnClickListener {
            val id = etAnimalId.text.toString().trim()
            val type = spType.selectedItem.toString()
            val weight = etWeight.text.toString().trim()
            val lastCheckup = etLastCheckup.text.toString().trim()

            if (id.isEmpty() || type.isEmpty() || weight.isEmpty()) {
                Toast.makeText(this, "Please fill all mandatory fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val animalMap = mapOf(
                "id" to id,
                "type" to type,
                "weight" to weight,
                "lastCheckup" to lastCheckup
            )

            val uid = auth.currentUser?.uid ?: return@setOnClickListener
            db.collection("users").document(uid)
                .collection("animals")
                .add(animalMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "$type added successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
