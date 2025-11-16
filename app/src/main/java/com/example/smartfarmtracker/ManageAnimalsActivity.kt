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
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnimalAdapter
    private var animalList = mutableListOf<Animal>()
    private lateinit var btnAddAnimal: ImageButton

    private lateinit var layoutAnimalTypes: LinearLayout
    private lateinit var btnCow: Button
    private lateinit var btnGoat: Button
    private lateinit var btnSheep: Button
    private lateinit var btnPig: Button
    private lateinit var btnPoultry: Button

    private var selectedType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_animals)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Animal type buttons layout
        layoutAnimalTypes = findViewById(R.id.layoutAnimalTypes)
        btnCow = findViewById(R.id.btnCow)
        btnGoat = findViewById(R.id.btnGoat)
        btnSheep = findViewById(R.id.btnSheep)
        btnPig = findViewById(R.id.btnPig)
        btnPoultry = findViewById(R.id.btnPoultry)

        recyclerView = findViewById(R.id.recyclerAnimals)
        btnAddAnimal = findViewById(R.id.btnAddAnimal)
        adapter = AnimalAdapter(animalList) { animal ->
            val intent = Intent(this, AnimalDetailActivity::class.java)
            intent.putExtra("animalId", animal.id)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Show animals of selected type
        val typeClickListener = View.OnClickListener { view ->
            selectedType = when (view.id) {
                R.id.btnCow -> "Cow"
                R.id.btnGoat -> "Goat"
                R.id.btnSheep -> "Sheep"
                R.id.btnPig -> "Pig"
                R.id.btnPoultry -> "Poultry"
                else -> ""
            }
            fetchAnimalsByType(selectedType)
        }

        btnCow.setOnClickListener(typeClickListener)
        btnGoat.setOnClickListener(typeClickListener)
        btnSheep.setOnClickListener(typeClickListener)
        btnPig.setOnClickListener(typeClickListener)
        btnPoultry.setOnClickListener(typeClickListener)

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

    private fun fetchAnimalsByType(type: String) {
        val uid = auth.currentUser?.uid ?: return

        db.collection("users").document(uid)
            .collection("animals")
            .whereEqualTo("type", type)
            .get()
            .addOnSuccessListener { result ->
                animalList.clear()
                for (doc in result) {
                    val animal = doc.toObject(Animal::class.java)
                    animal.id = doc.id
                    animalList.add(animal)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch animals", Toast.LENGTH_SHORT).show()
            }
    }
}
