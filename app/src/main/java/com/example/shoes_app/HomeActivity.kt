package com.example.shoes_app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class HomeActivity : AppCompatActivity() {
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var tvLocation: TextView
    private lateinit var tvProfileName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tvLocation = findViewById(R.id.tvLocation)
        tvProfileName = findViewById(R.id.tvProfileName)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        val etSearch = findViewById<EditText>(R.id.etSearch)
        
        // Find the notification icon in the header (ivNotificationsTop)
        val ivNotificationsBell = findViewById<ImageView>(R.id.ivNotificationsTop)

        setupManualProducts()
        setupDrawerAndNav(drawerLayout)
        loadUserData()

        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            getLocation()
        } catch (e: Exception) {
            Log.e("HomeActivity", "Location setup failed", e)
            tvLocation.text = getString(R.string.default_location)
        }

        btnMenu.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        etSearch.setOnClickListener { startActivity(Intent(this, SearchActivity::class.java)) }
        
        // Set click listener for the top notification icon
        ivNotificationsBell?.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }
    }

    private fun loadUserData() {
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("userId", null) ?: return

        FirebaseDatabase.getInstance().getReference("users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    user?.email?.let { email ->
                        val displayName = email.substringBefore("@")
                        tvProfileName.text = displayName
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("HomeActivity", "Failed to load user data", error.toException())
                }
            })
    }

    private fun setupManualProducts() {
        setupClick(R.id.shoe_card_1, "1", R.id.tvShoe1, R.id.tvPrice1)
        setupClick(R.id.shoe_card_2, "2", R.id.tvShoe2, R.id.tvPrice2)
        setupClick(R.id.shoe_card_3, "3", R.id.tvShoe3, R.id.tvPrice3)
        setupClick(R.id.shoe_card_4, "4", R.id.tvShoe4, R.id.tvPrice4)
        setupClick(R.id.popular_shoe_1, "5", R.id.tvPopular1, R.id.tvPricePopular1)
        setupClick(R.id.popular_shoe_2, "6", R.id.tvPopular2, R.id.tvPricePopular2)
    }

    private fun setupClick(cardId: Int, productId: String, nameId: Int, priceId: Int) {
        val card = findViewById<View>(cardId) ?: return
        val nameView = findViewById<TextView>(nameId)
        val priceView = findViewById<TextView>(priceId)

        card.setOnClickListener {
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("productId", productId)
            intent.putExtra("productName", nameView?.text.toString())
            intent.putExtra("productPrice", priceView?.text.toString().replace("₹", ""))
            intent.putExtra("isManual", true)
            startActivity(intent)
        }
    }

    private fun setupDrawerAndNav(drawerLayout: DrawerLayout) {
        // Bottom nav views
        val bottomNav = findViewById<View>(R.id.bottomNavInclude)
        val ivHome = bottomNav.findViewById<ImageView>(R.id.ivHome)
        val ivFav = bottomNav.findViewById<ImageView>(R.id.ivFav)
        val fabCart = bottomNav.findViewById<FloatingActionButton>(R.id.fabCart)
        val ivNotificationsBottom = bottomNav.findViewById<ImageView>(R.id.ivNotifications)
        val ivProfileNav = bottomNav.findViewById<ImageView>(R.id.ivProfileNav)

        ivHome.setColorFilter(ContextCompat.getColor(this, R.color.primary_blue))

        findViewById<TextView>(R.id.menu_home).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        findViewById<TextView>(R.id.menu_account).setOnClickListener {
            startActivity(Intent(this, AccountSettingsActivity::class.java))
        }
        findViewById<TextView>(R.id.menu_favorite).setOnClickListener {
            startActivity(Intent(this, FavouriteActivity::class.java))
        }
        findViewById<TextView>(R.id.menu_cart).setOnClickListener {
            startActivity(Intent(this, MyCartActivity::class.java))
        }
        findViewById<TextView>(R.id.menu_orders).setOnClickListener {
            startActivity(Intent(this, UserOrdersActivity::class.java))
        }
        findViewById<TextView>(R.id.menu_notifications).setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }
        findViewById<TextView>(R.id.menu_signout).setOnClickListener {
            getSharedPreferences("UserPrefs", Context.MODE_PRIVATE).edit {
                clear()
            }
            startActivity(Intent(this, SignInActivity::class.java))
            finishAffinity()
        }

        ivFav.setOnClickListener { startActivity(Intent(this, FavouriteActivity::class.java)) }
        fabCart.setOnClickListener { startActivity(Intent(this, MyCartActivity::class.java)) }
        ivNotificationsBottom.setOnClickListener { startActivity(Intent(this, NotificationsActivity::class.java)) }
        ivProfileNav.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            location?.let {
                val geocoder = Geocoder(this, Locale.getDefault())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(it.latitude, it.longitude, 1) { addresses ->
                        if (addresses.isNotEmpty()) {
                            runOnUiThread {
                                tvLocation.text = getString(R.string.location_format, addresses[0].locality, addresses[0].countryName)
                            }
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        tvLocation.text = getString(R.string.location_format, addresses[0].locality, addresses[0].countryName)
                    }
                }
            }
        }
    }
}
