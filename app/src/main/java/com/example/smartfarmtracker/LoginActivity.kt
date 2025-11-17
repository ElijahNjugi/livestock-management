package com.example.smartfarmtracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailEditText = findViewById<EditText>(R.id.etLoginEmail)
        val passwordEditText = findViewById<EditText>(R.id.etLoginPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val registerRedirect = findViewById<TextView>(R.id.tvRegisterRedirect)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase sign-in
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser

                        if (currentUser != null) {
                            val userId = currentUser.uid

                            // Fetch first name from Firestore
                            db.collection("users").document(userId).get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val firstName = document.getString("firstName") ?: "User"

                                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                                        val intent = Intent(this, HomeActivity::class.java)
                                        intent.putExtra("firstName", firstName)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "User data not found", Toast.LENGTH_LONG).show()
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Failed to fetch profile", Toast.LENGTH_LONG).show()
                                }

                        }

                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        registerRedirect.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
