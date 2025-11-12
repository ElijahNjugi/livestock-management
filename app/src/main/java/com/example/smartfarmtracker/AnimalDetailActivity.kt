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
import androidx.viewpager2.widget.ViewPager2
import com.example.smartfarmtracker.model.Animal
import com.example.smartfarmtracker.AnimalFragmentsAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class AnimalDetailActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var animalId: String
    private var listener: ListenerRegistration? = null

    private lateinit var nameText: TextView
    private lateinit var typeText: TextView
    private lateinit var breedText: TextView
    private lateinit var ageText: TextView
    private lateinit var genderText: TextView
    private lateinit var healthText: TextView
    private lateinit var weightText: TextView
    private lateinit var feedText: TextView
    private lateinit var notesText: TextView
    private lateinit var statusText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_detail)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Views
        nameText = findViewById(R.id.animalNameText)
        typeText = findViewById(R.id.animalTypeText)
        breedText = findViewById(R.id.animalBreedText)
        ageText = findViewById(R.id.animalAgeText)
        genderText = findViewById(R.id.animalGenderText)
        healthText = findViewById(R.id.animalHealthText)
        weightText = findViewById(R.id.animalWeightText)
        feedText = findViewById(R.id.animalFeedText)
        notesText = findViewById(R.id.animalNotesText)
        statusText = findViewById(R.id.animalStatusText)
        progressBar = findViewById(R.id.progressBar)
        editButton = findViewById(R.id.editButton)
        deleteButton = findViewById(R.id.deleteButton)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        animalId = intent.getStringExtra("animalId") ?: ""
        val userId = auth.currentUser?.uid

        if (userId.isNullOrEmpty() || animalId.isEmpty()) {
            Toast.makeText(this, "Invalid user or animal ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Set up fragments in ViewPager
        val adapter = AnimalFragmentsAdapter(this, animalId)
        viewPager.adapter = adapter

        // Attach TabLayout to ViewPager
        val tabTitles = arrayOf("Feeding", "Medical", "Notes", "Sales")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        fetchAnimalDetails(userId, animalId)

        editButton.setOnClickListener {
            startActivity(Intent(this, EditAnimalActivity::class.java).apply {
                putExtra("animalId", animalId)
            })
        }

        deleteButton.setOnClickListener {
            confirmDelete(userId, animalId)
        }
    }

    private fun fetchAnimalDetails(userId: String, animalId: String) {
        progressBar.visibility = View.VISIBLE

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
                    if (animal != null) populateAnimalDetails(animal)
                } else {
                    Toast.makeText(this, "Animal not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
    }

    private fun populateAnimalDetails(animal: Animal) {
        nameText.text = animal.name
        typeText.text = "Type: ${animal.type}"
        breedText.text = "Breed: ${animal.breed}"
        ageText.text = "Age: ${animal.age}"
        genderText.text = "Gender: ${animal.gender}"
        healthText.text = "Health: ${animal.healthStatus}"
        weightText.text = "Weight: ${animal.weightKg} kg"
        feedText.text = "Feed Type: ${animal.feedType}"
        notesText.text = "Notes: ${animal.notes}"
        statusText.text = "Status: ${animal.status}"
    }

    private fun confirmDelete(userId: String, animalId: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete Animal")
            .setMessage("Are you sure you want to delete this animal?")
            .setPositiveButton("Yes") { _, _ ->
                db.collection("users").document(userId)
                    .collection("animals").document(animalId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Animal deleted", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.remove()
    }
}
