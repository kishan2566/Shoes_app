package com.example.shoes_app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {
    private lateinit var ivProfile: ImageView
    private lateinit var tvProfileName: TextView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private var imageUri: Uri? = null
    private var currentUserId: String? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data?.data
            ivProfile.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        tvProfileName = findViewById(R.id.tvProfileName)
        ivProfile = findViewById(R.id.ivProfile)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val profileImageContainer = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.profileImageContainer)

        btnBack.setOnClickListener { finish() }

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        currentUserId = sharedPref.getString("userId", null)

        if (currentUserId != null) {
            val database = FirebaseDatabase.getInstance()
            val userRef = database.getReference("users").child(currentUserId!!)

            // Load profile data
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        etName.setText(user.name)
                        etEmail.setText(user.email)
                        etPassword.setText(user.password)
                        tvProfileName.text = user.name
                        
                        if (!user.profileImageUrl.isNullOrEmpty()) {
                            Glide.with(this@ProfileActivity)
                                .load(user.profileImageUrl)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .circleCrop()
                                .into(ivProfile)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            profileImageContainer.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImage.launch(intent)
            }

            // Save changes
            btnSave.setOnClickListener {
                saveProfileChanges()
            }
        }
    }

    private fun saveProfileChanges() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            if (imageUri != null) {
                uploadImageAndSaveData(name, email, password)
            } else {
                updateUserData(name, email, password, null)
            }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageAndSaveData(name: String, email: String, password: String) {
        val storageRef = FirebaseStorage.getInstance().getReference("profile_images/$currentUserId.jpg")
        
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    updateUserData(name, email, password, uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Image upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserData(name: String, email: String, password: String, imageUrl: String?) {
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId!!)
        val updates = mutableMapOf<String, Any>(
            "name" to name,
            "email" to email,
            "password" to password
        )
        if (imageUrl != null) {
            updates["profileImageUrl"] = imageUrl
        }

        userRef.updateChildren(updates).addOnSuccessListener {
            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
            tvProfileName.text = name
        }.addOnFailureListener {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }
    }
}