package com.example.shoes_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase

class AccountSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val swPushNotif = findViewById<SwitchCompat>(R.id.swPushNotif)
        val swLocation = findViewById<SwitchCompat>(R.id.swLocation)
        val rowShipping = findViewById<RelativeLayout>(R.id.rowShipping)
        val rowDelete = findViewById<RelativeLayout>(R.id.rowDelete)

        btnBack.setOnClickListener {
            finish()
        }

        // Navigation to Shipping Address
        rowShipping.setOnClickListener {
            startActivity(Intent(this, ShippingAddressActivity::class.java))
        }

        // Delete Account Logic
        rowDelete.setOnClickListener {
            showDeleteAccountDialog()
        }

        // SharedPrefs for settings
        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        
        // Push Notification Switch
        swPushNotif.isChecked = sharedPref.getBoolean("push_notif", true)
        swPushNotif.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) "Push notifications enabled" else "Push notifications disabled"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            sharedPref.edit().putBoolean("push_notif", isChecked).apply()
        }

        // Location Switch
        swLocation.isChecked = sharedPref.getBoolean("location_enabled", true)
        swLocation.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("location_enabled", isChecked).apply()
        }

        setupBottomNav()
    }

    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                deleteUserAccount()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteUserAccount() {
        val userPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = userPrefs.getString("userId", null)

        if (userId != null) {
            val database = FirebaseDatabase.getInstance()
            database.getReference("users").child(userId).removeValue()
                .addOnSuccessListener {
                    // Clear local data and logout
                    userPrefs.edit().clear().apply()
                    Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_LONG).show()
                    
                    val intent = Intent(this, SignInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to delete account. Try again.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setupBottomNav() {
        val ivHome = findViewById<ImageView>(R.id.ivHome)
        val ivFav = findViewById<ImageView>(R.id.ivFav)
        val fabCart = findViewById<FloatingActionButton>(R.id.fabCart)
        val ivNotifications = findViewById<ImageView>(R.id.ivNotifications)
        val ivProfileNav = findViewById<ImageView>(R.id.ivProfileNav)

        ivProfileNav.setColorFilter(ContextCompat.getColor(this, R.color.primary_blue))

        ivHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        ivFav.setOnClickListener {
            startActivity(Intent(this, FavouriteActivity::class.java))
            finish()
        }

        fabCart.setOnClickListener {
            startActivity(Intent(this, MyCartActivity::class.java))
        }

        ivNotifications.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
            finish()
        }
    }
}
