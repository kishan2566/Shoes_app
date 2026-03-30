package com.example.shoes_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var tvTotalRevenue: TextView
    private lateinit var tvActiveOrders: TextView
    private lateinit var tvTotalUsers: TextView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        database = FirebaseDatabase.getInstance().reference
        
        val drawerLayout = findViewById<DrawerLayout>(R.id.adminDrawerLayout)
        val btnMenu = findViewById<ImageButton>(R.id.btnAdminMenu)
        val navView = findViewById<NavigationView>(R.id.adminNavView)
        
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue)
        tvActiveOrders = findViewById(R.id.tvActiveOrders)
        tvTotalUsers = findViewById(R.id.tvTotalUsers)

        btnMenu.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }

        // Logout button
        findViewById<ImageButton>(R.id.btnAdminLogout).setOnClickListener {
            val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            sharedPref.edit().remove("userId").apply()
            startActivity(Intent(this, SignInActivity::class.java))
            finishAffinity()
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_admin_home -> drawerLayout.closeDrawer(GravityCompat.START)
                R.id.menu_manage_products -> {
                    startActivity(Intent(this, ManageProductsActivity::class.java))
                }
                R.id.menu_manage_orders -> {
                    startActivity(Intent(this, ManageOrdersActivity::class.java))
                }
                R.id.menu_manage_users -> {
                    startActivity(Intent(this, ManageUsersActivity::class.java))
                }
                R.id.menu_user_view -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                R.id.menu_add_product -> {
                    startActivity(Intent(this, AddProductActivity::class.java))
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        fetchStats()
    }

    private fun fetchStats() {
        // Fetch Total Revenue & Active Orders
        database.child("orders").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalRevenue = 0.0
                var activeOrders = 0
                for (orderSnap in snapshot.children) {
                    val order = orderSnap.getValue(Order::class.java)
                    if (order != null) {
                        totalRevenue += order.totalPrice ?: 0.0
                        if (order.status != "Delivered" && order.status != "Cancelled") {
                            activeOrders++
                        }
                    }
                }
                tvTotalRevenue.text = "₹${String.format("%.2f", totalRevenue)}"
                tvActiveOrders.text = activeOrders.toString()
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        // Fetch Total Users
        database.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tvTotalUsers.text = snapshot.childrenCount.toString()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
