package com.example.smartfarmtracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var btnBack: ImageButton
    private lateinit var recyclerAnimals: RecyclerView

    private var selectedType: String = ""
    private val animalList = mutableListOf<Animal>()

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
        btnBack = findViewById(R.id.btnBack)
        recyclerAnimals = findViewById(R.id.recyclerAnimals)

        recyclerAnimals.layoutManager = LinearLayoutManager(this)

        loadAnimals() // Load all animals initially

        // Filter animals by type when clicking buttons
        val typeClickListener = View.OnClickListener { view ->
            selectedType = when (view.id) {
                R.id.btnCow -> "Cow"
                R.id.btnGoat -> "Goat"
                R.id.btnSheep -> "Sheep"
                R.id.btnPig -> "Pig"
                R.id.btnPoultry -> "Poultry"
                else -> ""
            }

            filterAnimalsByType(selectedType)
        }

        btnCow.setOnClickListener(typeClickListener)
        btnGoat.setOnClickListener(typeClickListener)
        btnSheep.setOnClickListener(typeClickListener)
        btnPig.setOnClickListener(typeClickListener)
        btnPoultry.setOnClickListener(typeClickListener)

        // Go back
        btnBack.setOnClickListener { finish() }

        // Add new animal
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

    /** Load all animals for the current user */
    private fun loadAnimals() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId)
            .collection("animals")
            .get()
            .addOnSuccessListener { snapshot ->
                animalList.clear()
                for (doc in snapshot.documents) {
                    val animal = doc.toObject(Animal::class.java)
                    if (animal != null) animalList.add(animal)
                }
                recyclerAnimals.adapter = AnimalAdapter(animalList) { animal ->
                    openAnimalDetails(animal)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load animals", Toast.LENGTH_SHORT).show()
            }
    }

    /** Filter displayed animals by type */
    private fun filterAnimalsByType(type: String) {
        val filtered = animalList.filter { it.type == type }
        recyclerAnimals.adapter = AnimalAdapter(filtered) { animal ->
            openAnimalDetails(animal)
        }
    }

    /** Open detail page for selected animal */
    private fun openAnimalDetails(animal: Animal) {
        val intent = Intent(this, AnimalDetailActivity::class.java)
        intent.putExtra("animalId", animal.id)
        startActivity(intent)
    }
}
