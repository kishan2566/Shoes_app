package com.example.shoes_app

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val totalAmount = intent.getDoubleExtra("totalAmount", 0.0)
        val tvTotalAmount = findViewById<TextView>(R.id.tvTotalAmount)
        tvTotalAmount.text = "$${String.format("%.2f", totalAmount)}"

        val etCardNumber = findViewById<EditText>(R.id.etCardNumber)
        val etExpiry = findViewById<EditText>(R.id.etExpiry)
        val etCvv = findViewById<EditText>(R.id.etCvv)
        val btnPayNow = findViewById<Button>(R.id.btnPayNow)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        btnPayNow.setOnClickListener {
            val card = etCardNumber.text.toString()
            val expiry = etExpiry.text.toString()
            val cvv = etCvv.text.toString()

            if (card.length == 16 && expiry.isNotEmpty() && cvv.length == 3) {
                processOrder()
            } else {
                Toast.makeText(this, "Please enter valid card details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun processOrder() {
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("userId", null) ?: return
        
        val cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userId)
        val orderRef = FirebaseDatabase.getInstance().getReference("orders")
        
        cartRef.get().addOnSuccessListener { snapshot ->
            val products = mutableListOf<Product>()
            var subtotal = 0.0
            for (child in snapshot.children) {
                val p = child.getValue(Product::class.java)
                if (p != null) {
                    products.add(p)
                    subtotal += p.price ?: 0.0
                }
            }

            if (products.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
                finish()
                return@addOnSuccessListener
            }

            val orderId = orderRef.push().key ?: return@addOnSuccessListener
            val total = subtotal + 40.90 // shipping cost matching MyCartActivity

            val order = Order(
                orderId = orderId,
                userId = userId,
                products = products,
                totalPrice = total,
                status = "Paid",
                timestamp = System.currentTimeMillis()
            )

            orderRef.child(orderId).setValue(order).addOnSuccessListener {
                cartRef.removeValue().addOnSuccessListener {
                    Toast.makeText(this, "Payment successful! Order placed.", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
}