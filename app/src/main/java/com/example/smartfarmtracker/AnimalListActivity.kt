package com.example.smartfarmtracker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.smartfarmtracker.model.Animal


class AnimalListActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnimalAdapter
    private var animalList = mutableListOf<Animal>()
    private lateinit var btnAddAnimal: ImageButton
    private var animalType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_list)

        animalType = intent.getStringExtra("animalType") ?: ""

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.recyclerAnimals)
        btnAddAnimal = findViewById(R.id.btnAddAnimal)

        adapter = AnimalAdapter(animalList) { animal ->
            val intent = Intent(this, AnimalDetailActivity::class.java)
            intent.putExtra("animalId", animal.id)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchAnimals()

        btnAddAnimal.setOnClickListener {
            val intent = Intent(this, AddAnimalActivity::class.java)
            intent.putExtra("animalType", animalType)
            startActivity(intent)
        }
    }

    private fun fetchAnimals() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users")
            .document(uid)
            .collection("animals")
            .whereEqualTo("type", animalType)
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
    }
}
