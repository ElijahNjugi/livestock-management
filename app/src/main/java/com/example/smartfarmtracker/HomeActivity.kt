package com.example.smartfarmtracker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {

    private lateinit var greetingText: TextView
    private lateinit var profileIcon: ImageView

    private lateinit var cardTotalAnimals: MaterialCardView
    private lateinit var cardSales: MaterialCardView
    private lateinit var cardReports: MaterialCardView

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        greetingText = findViewById(R.id.tvGreeting)
        profileIcon = findViewById(R.id.ivProfile)

        cardTotalAnimals = findViewById(R.id.cardTotalAnimals)
        cardSales = findViewById(R.id.cardSales)
        cardReports = findViewById(R.id.cardReports)

        bottomNav = findViewById(R.id.bottomNavigation)

        val userName = "User"
        greetingText.text = "Welcome Back, $userName"

        cardTotalAnimals.setOnClickListener {
            startActivity(Intent(this, ManageAnimalsActivity::class.java))
        }

        cardSales.setOnClickListener {
            startActivity(Intent(this, SalesActivity::class.java))
        }

        cardReports.setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java))
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            startActivity(intent)
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
