package com.example.smartfarmtracker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var tvFirstName: TextView
    private lateinit var tvLastName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvAge: TextView
    private lateinit var btnChangePassword: Button
    private lateinit var btnEditDetails: Button

    private var userDob: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        tvFirstName = findViewById(R.id.tvFirstName)
        tvLastName = findViewById(R.id.tvLastName)
        tvEmail = findViewById(R.id.tvEmail)
        tvAge = findViewById(R.id.tvAge)
        btnChangePassword = findViewById(R.id.btnChangePassword)
        btnEditDetails = findViewById(R.id.btnEditDetails)

        fetchUserProfile()

        btnChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        btnEditDetails.setOnClickListener {
            showEditDetailsDialog()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_profile  // highlight profile

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }
    }

    private fun fetchUserProfile() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val firstName = document.getString("firstName") ?: ""
                    val lastName = document.getString("lastName") ?: ""
                    val email = document.getString("email") ?: ""
                    userDob = document.getString("dob") ?: ""

                    tvFirstName.text = firstName
                    tvLastName.text = lastName
                    tvEmail.text = email
                    tvAge.text = calculateAge(userDob)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to fetch profile: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun calculateAge(dobString: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val dob = sdf.parse(dobString)
            val dobCalendar = Calendar.getInstance().apply { time = dob!! }
            val today = Calendar.getInstance()
            var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) age--
            "$age years"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    private fun showChangePasswordDialog() {
        val editText = EditText(this)
        editText.hint = "New Password"

        AlertDialog.Builder(this)
            .setTitle("Change Password")
            .setView(editText)
            .setPositiveButton("Update") { _, _ ->
                val newPassword = editText.text.toString()
                if (newPassword.length < 6) {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                auth.currentUser?.updatePassword(newPassword)
                    ?.addOnSuccessListener {
                        Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                    }
                    ?.addOnFailureListener { e ->
                        Toast.makeText(this, "Error updating password: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditDetailsDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_details, null)

        val etFirstName = dialogView.findViewById<EditText>(R.id.etEditFirstName)
        val etLastName = dialogView.findViewById<EditText>(R.id.etEditLastName)
        val etDob = dialogView.findViewById<EditText>(R.id.etEditDob)

        etFirstName.setText(tvFirstName.text.toString())
        etLastName.setText(tvLastName.text.toString())
        etDob.setText(userDob)

        AlertDialog.Builder(this)
            .setTitle("Edit Profile Details")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                updateUserDetails(
                    etFirstName.text.toString(),
                    etLastName.text.toString(),
                    etDob.text.toString()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateUserDetails(firstName: String, lastName: String, dob: String) {
        val uid = auth.currentUser?.uid ?: return

        val userMap = mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "dob" to dob,
            "email" to tvEmail.text.toString()
        )

        db.collection("users").document(uid)
            .update(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Details updated!", Toast.LENGTH_SHORT).show()
                fetchUserProfile()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Update failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
