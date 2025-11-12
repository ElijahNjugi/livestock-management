package com.example.smartfarmtracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartfarmtracker.model.Animal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditAnimalActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var animalId: String

    // UI elements
    private lateinit var nameField: EditText
    private lateinit var typeField: EditText
    private lateinit var breedField: EditText
    private lateinit var genderSpinner: Spinner
    private lateinit var ageField: EditText
    private lateinit var healthSpinner: Spinner
    private lateinit var weightField: EditText
    private lateinit var feedField: EditText
    private lateinit var dailyFeedField: EditText
    private lateinit var milkField: EditText
    private lateinit var lastFeedDateField: EditText
    private lateinit var nextCheckupDateField: EditText
    private lateinit var vaccinationsField: EditText
    private lateinit var medicationsField: EditText
    private lateinit var diseasesField: EditText
    private lateinit var offspringField: EditText
    private lateinit var notesField: EditText
    private lateinit var idField: EditText
    private lateinit var updateButton: Button

    private val genders = arrayOf("Male", "Female", "Unknown")
    private val healthStatuses = arrayOf("Healthy", "Sick", "Dead")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_animal)

        // Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Views
        nameField = findViewById(R.id.editName)
        typeField = findViewById(R.id.editType)
        breedField = findViewById(R.id.editBreed)
        genderSpinner = findViewById(R.id.editGenderSpinner)
        ageField = findViewById(R.id.editAge)
        healthSpinner = findViewById(R.id.editHealthSpinner)
        weightField = findViewById(R.id.editWeight)
        feedField = findViewById(R.id.editFeedType)
        dailyFeedField = findViewById(R.id.editDailyFeedQuantity)
        milkField = findViewById(R.id.editMilkProduction)
        lastFeedDateField = findViewById(R.id.editLastFeedDate)
        nextCheckupDateField = findViewById(R.id.editNextCheckupDate)
        vaccinationsField = findViewById(R.id.editVaccinations)
        medicationsField = findViewById(R.id.editMedications)
        diseasesField = findViewById(R.id.editDiseases)
        offspringField = findViewById(R.id.editOffspring)
        notesField = findViewById(R.id.editNotes)
        idField = findViewById(R.id.editAnimalId)
        updateButton = findViewById(R.id.buttonUpdate)

        // Spinner adapters
        genderSpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)
        healthSpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, healthStatuses)

        animalId = intent.getStringExtra("animalId") ?: ""
        val userId = auth.currentUser?.uid

        if (userId.isNullOrEmpty() || animalId.isEmpty()) {
            Toast.makeText(this, "Invalid user or animal ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val docRef = db.collection("users").document(userId)
            .collection("animals").document(animalId)

        // Load animal data
        docRef.get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                val animal = doc.toObject(Animal::class.java)
                if (animal != null) {
                    nameField.setText(animal.name)
                    typeField.setText(animal.type)
                    breedField.setText(animal.breed)
                    genderSpinner.setSelection(genders.indexOf(animal.gender).takeIf { it >= 0 } ?: 2)
                    ageField.setText(animal.age.toString())
                    healthSpinner.setSelection(healthStatuses.indexOf(animal.healthStatus).takeIf { it >= 0 } ?: 0)
                    weightField.setText(animal.weightKg.toString())
                    feedField.setText(animal.feedType)
                    dailyFeedField.setText(animal.dailyFeedQuantityKg.toString())
                    milkField.setText(animal.milkProductionLiters.toString())
                    lastFeedDateField.setText(animal.lastFeedDate)
                    nextCheckupDateField.setText(animal.nextCheckupDate)
                    vaccinationsField.setText(animal.vaccinations.joinToString(", "))
                    medicationsField.setText(animal.medications.joinToString(", "))
                    diseasesField.setText(animal.diseases.joinToString(", "))
                    offspringField.setText(animal.offspring.joinToString(", "))
                    notesField.setText(animal.notes)
                    idField.setText(animal.id)
                }
            } else {
                Toast.makeText(this, "Animal not found", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load animal details", Toast.LENGTH_SHORT).show()
        }

        // Update button click
        updateButton.setOnClickListener {
            val ageInt = ageField.text.toString().toIntOrNull() ?: 0
            val weightFloat = weightField.text.toString().toFloatOrNull() ?: 0f
            val dailyFeedFloat = dailyFeedField.text.toString().toFloatOrNull() ?: 0f
            val milkFloat = milkField.text.toString().toFloatOrNull() ?: 0f

            val updatedData = mapOf(
                "name" to nameField.text.toString().ifEmpty { "Unnamed" },
                "type" to typeField.text.toString(),
                "breed" to breedField.text.toString(),
                "gender" to genderSpinner.selectedItem.toString(),
                "age" to ageInt,
                "healthStatus" to healthSpinner.selectedItem.toString(),
                "weightKg" to weightFloat,
                "feedType" to feedField.text.toString(),
                "dailyFeedQuantityKg" to dailyFeedFloat,
                "milkProductionLiters" to milkFloat,
                "lastFeedDate" to lastFeedDateField.text.toString(),
                "nextCheckupDate" to nextCheckupDateField.text.toString(),
                "vaccinations" to vaccinationsField.text.toString().split(",").map { it.trim() },
                "medications" to medicationsField.text.toString().split(",").map { it.trim() },
                "diseases" to diseasesField.text.toString().split(",").map { it.trim() },
                "offspring" to offspringField.text.toString().split(",").map { it.trim() },
                "notes" to notesField.text.toString(),
                "updatedAt" to System.currentTimeMillis()
            )

            docRef.update(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Animal updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update animal", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
