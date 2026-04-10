package com.example.shoes_app

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class ShippingAddressActivity : AppCompatActivity() {
    private lateinit var etFullName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var etCity: EditText
    private lateinit var etPincode: EditText
    private lateinit var database: DatabaseReference
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipping_address)

        etFullName = findViewById(R.id.etFullName)
        etPhone = findViewById(R.id.etPhone)
        etAddress = findViewById(R.id.etAddress)
        etCity = findViewById(R.id.etCity)
        etPincode = findViewById(R.id.etPincode)
        val btnSave = findViewById<Button>(R.id.btnSaveAddress)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userId = sharedPref.getString("userId", null)

        if (userId == null) {
            finish()
            return
        }

        database = FirebaseDatabase.getInstance().getReference("addresses").child(userId!!)

        loadExistingAddress()

        btnSave.setOnClickListener {
            saveAddress()
        }
    }

    private fun loadExistingAddress() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    etFullName.setText(snapshot.child("fullName").value?.toString())
                    etPhone.setText(snapshot.child("phone").value?.toString())
                    etAddress.setText(snapshot.child("address").value?.toString())
                    etCity.setText(snapshot.child("city").value?.toString())
                    etPincode.setText(snapshot.child("pincode").value?.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun saveAddress() {
        val fullName = etFullName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val city = etCity.text.toString().trim()
        val pincode = etPincode.text.toString().trim()

        if (fullName.isEmpty() || phone.isEmpty() || address.isEmpty() || city.isEmpty() || pincode.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val addressMap = mapOf(
            "fullName" to fullName,
            "phone" to phone,
            "address" to address,
            "city" to city,
            "pincode" to pincode
        )

        database.setValue(addressMap).addOnSuccessListener {
            Toast.makeText(this, "Address saved successfully", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to save address", Toast.LENGTH_SHORT).show()
        }
    }
}
