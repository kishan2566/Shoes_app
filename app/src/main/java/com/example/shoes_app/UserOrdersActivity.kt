package com.example.shoes_app

import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class UserOrdersActivity : AppCompatActivity() {
    private lateinit var adapter: OrderAdapter
    private lateinit var database: DatabaseReference
    private val orderList = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_orders)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        val recyclerView = findViewById<RecyclerView>(R.id.rvUserOrders)
        recyclerView.layoutManager = LinearLayoutManager(this)

        database = FirebaseDatabase.getInstance().getReference("orders")

        // Allow user to mark order as Delivered
        adapter = OrderAdapter(orderList) { order ->
            updateOrderStatusToDelivered(order)
        }
        recyclerView.adapter = adapter

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("userId", null)

        if (userId != null) {
            fetchUserOrders(userId)
        } else {
            finish()
        }
    }

    private fun updateOrderStatusToDelivered(order: Order) {
        val orderId = order.orderId ?: return
        database.child(orderId).child("status").setValue("Delivered")
            .addOnSuccessListener {
                Toast.makeText(this, "Order marked as Delivered", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchUserOrders(userId: String) {
        database.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    orderList.clear()
                    for (orderSnapshot in snapshot.children) {
                        val order = orderSnapshot.getValue(Order::class.java)
                        if (order != null) {
                            orderList.add(order)
                        }
                    }
                    orderList.sortByDescending { it.timestamp }
                    adapter.updateOrders(orderList)
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@UserOrdersActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
