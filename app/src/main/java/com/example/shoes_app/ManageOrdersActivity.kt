package com.example.shoes_app

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ManageOrdersActivity : AppCompatActivity() {
    private lateinit var adapter: OrderAdapter
    private lateinit var database: DatabaseReference
    private val orderList = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_orders)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        val recyclerView = findViewById<RecyclerView>(R.id.rvOrders)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = OrderAdapter(orderList) { order ->
            updateOrderStatus(order)
        }
        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("orders")
        fetchOrders()
    }

    private fun fetchOrders() {
        database.addValueEventListener(object : ValueEventListener {
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
                Toast.makeText(this@ManageOrdersActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateOrderStatus(order: Order) {
        order.orderId?.let { id ->
            database.child(id).child("status").setValue("Delivered")
                .addOnSuccessListener {
                    Toast.makeText(this, "Order marked as Delivered", Toast.LENGTH_SHORT).show()
                }
        }
    }
}