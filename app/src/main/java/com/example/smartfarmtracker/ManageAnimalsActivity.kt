package com.example.smartfarmtracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ManageAnimalsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_animals)

        val btnCow = findViewById<Button>(R.id.btnCow)
        val btnGoat = findViewById<Button>(R.id.btnGoat)
        val btnSheep = findViewById<Button>(R.id.btnSheep)
        val btnPig = findViewById<Button>(R.id.btnPig)
        val btnPoultry = findViewById<Button>(R.id.btnPoultry)

        val listener = { animalType: String ->
            val intent = Intent(this, AnimalListActivity::class.java)
            intent.putExtra("animalType", animalType)
            startActivity(intent)
        }

        btnCow.setOnClickListener { listener("Cow") }
        btnGoat.setOnClickListener { listener("Goat") }
        btnSheep.setOnClickListener { listener("Sheep") }
        btnPig.setOnClickListener { listener("Pig") }
        btnPoultry.setOnClickListener { listener("Poultry") }
    }
}
