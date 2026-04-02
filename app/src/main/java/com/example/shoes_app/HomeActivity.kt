package com.example.shoes_app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class HomeActivity : AppCompatActivity() {
    private lateinit var popularAdapter: HomeProductAdapter
    private lateinit var arrivalsAdapter: HomeProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private val popularList = mutableListOf<Product>()
    private val arrivalsList = mutableListOf<Product>()
    private val allProducts = mutableListOf<Product>()
    private val categories = listOf("All Shoes", "Nike", "Adidas", "Puma", "Under Armour", "Jordan")
    
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var tvLocation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        tvLocation = findViewById(R.id.tvLocation)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        val btnCart = findViewById<ImageButton>(R.id.btnCart)
        val etSearch = findViewById<EditText>(R.id.etSearch)
        val tvSeeAll = findViewById<TextView>(R.id.tvSeeAll)
        val tvSeeAllArrivals = findViewById<TextView>(R.id.tvSeeAllArrivals)
        
        // RecyclerViews
        val rvCategories = findViewById<RecyclerView>(R.id.rvCategories)
        val rvPopular = findViewById<RecyclerView>(R.id.rvPopularShoes)
        val rvArrivals = findViewById<RecyclerView>(R.id.rvNewArrivals)

        // Categories Setup
        rvCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter = CategoryAdapter(categories) { category ->
            filterProducts(category)
        }
        rvCategories.adapter = categoryAdapter

        rvPopular.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvArrivals.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        popularAdapter = HomeProductAdapter(popularList, this)
        arrivalsAdapter = HomeProductAdapter(arrivalsList, this)

        rvPopular.adapter = popularAdapter
        rvArrivals.adapter = arrivalsAdapter

        // Fetch Products
        fetchProducts()
        
        // Get Live Location
        getLocation()

        // Bottom Nav Icons
        val ivHome = findViewById<ImageView>(R.id.ivHome)
        val ivFav = findViewById<ImageView>(R.id.ivFav)
        val fabCart = findViewById<FloatingActionButton>(R.id.fabCart)
        val ivNotifications = findViewById<ImageView>(R.id.ivNotifications)
        val ivProfileNav = findViewById<ImageView>(R.id.ivProfileNav)

        ivHome.setColorFilter(ContextCompat.getColor(this, R.color.primary_blue))

        // Drawer Menu Items
        val menuAccount = findViewById<TextView>(R.id.menu_account)
        val menuHome = findViewById<TextView>(R.id.menu_home)
        val menuCart = findViewById<TextView>(R.id.menu_cart)
        val menuFavorite = findViewById<TextView>(R.id.menu_favorite)
        val menuOrders = findViewById<TextView>(R.id.menu_orders)
        val menuNotifications = findViewById<TextView>(R.id.menu_notifications)
        val menuAdmin = findViewById<TextView>(R.id.menu_admin)
        val menuSignOut = findViewById<TextView>(R.id.menu_signout)
        val tvDrawerName = findViewById<TextView>(R.id.tvProfileName)
        val ivDrawerProfile = findViewById<ImageView>(R.id.ivProfile)

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("userId", null)

        if (userId != null) {
            val database = FirebaseDatabase.getInstance()
            database.getReference("users").child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)
                        if (user != null) {
                            tvDrawerName.text = user.name
                            menuAdmin.visibility = if (user.isAdmin) View.VISIBLE else View.GONE
                            
                            if (!user.profileImageUrl.isNullOrEmpty()) {
                                Glide.with(this@HomeActivity)
                                    .load(user.profileImageUrl)
                                    .placeholder(android.R.drawable.ic_menu_gallery)
                                    .circleCrop()
                                    .into(ivDrawerProfile)
                                
                                Glide.with(this@HomeActivity)
                                    .load(user.profileImageUrl)
                                    .placeholder(R.drawable.ic_profile)
                                    .circleCrop()
                                    .into(ivProfileNav)
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
        }

        btnMenu.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        etSearch.setOnClickListener { startActivity(Intent(this, SearchActivity::class.java)) }
        tvSeeAll.setOnClickListener { startActivity(Intent(this, BestSellerActivity::class.java)) }
        tvSeeAllArrivals.setOnClickListener { startActivity(Intent(this, BestSellerActivity::class.java)) }
        btnCart.setOnClickListener { startActivity(Intent(this, MyCartActivity::class.java)) }
        fabCart.setOnClickListener { startActivity(Intent(this, MyCartActivity::class.java)) }
        ivFav.setOnClickListener { startActivity(Intent(this, FavouriteActivity::class.java)) }
        ivNotifications.setOnClickListener { startActivity(Intent(this, NotificationsActivity::class.java)) }
        ivProfileNav.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }

        menuAccount.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, AccountSettingsActivity::class.java))
        }
        menuHome.setOnClickListener { drawerLayout.closeDrawer(GravityCompat.START) }
        menuCart.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, MyCartActivity::class.java))
        }
        menuFavorite.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, FavouriteActivity::class.java))
        }
        menuOrders.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, UserOrdersActivity::class.java))
        }
        menuNotifications.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, NotificationsActivity::class.java))
        }
        menuAdmin.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, AdminDashboardActivity::class.java))
        }
        menuSignOut.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            sharedPref.edit().remove("userId").apply()
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val city = addresses[0].locality
                    val country = addresses[0].countryName
                    tvLocation.text = "$city, $country"
                }
            } else {
                tvLocation.text = "Location not found"
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        }
    }

    private fun fetchProducts() {
        allProducts.clear()
        allProducts.addAll(ProductRepository.products)
        filterProducts("All Shoes")
    }

    private fun filterProducts(category: String) {
        popularList.clear()
        arrivalsList.clear()
        
        if (category == "All Shoes") {
            popularList.addAll(allProducts)
            arrivalsList.addAll(allProducts.reversed())
        } else {
            val filtered = allProducts.filter { it.category?.equals(category, ignoreCase = true) == true }
            popularList.addAll(filtered)
            arrivalsList.addAll(filtered.reversed())
        }
        
        popularAdapter.updateProducts(popularList)
        arrivalsAdapter.updateProducts(arrivalsList)
        
        if (popularList.isEmpty()) {
            Toast.makeText(this, "No shoes found for $category", Toast.LENGTH_SHORT).show()
        }
    }
}