package com.example.shoes_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnSignIn) // Using the same ID from XML for Registration

        // Back button to Sign In
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Sign In link footer
        findViewById<TextView>(R.id.tvFooter).setOnClickListener {
            finish()
        }

        // Firebase Realtime Database reference
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")

        // Register button logic
        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = usersRef.push().key ?: return@setOnClickListener
            val user = User(name, email, password)

            usersRef.child(userId).setValue(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Registration Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    data class User(val name: String, val email: String, val password: String)
}