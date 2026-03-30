package com.example.shoes_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.*

class ProductDetailsActivity : AppCompatActivity() {
    private var product: Product? = null
    private var selectedSize: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val productId = intent.getStringExtra("productId")
        if (productId == null) {
            Toast.makeText(this, "Product ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val ivProduct = findViewById<ImageView>(R.id.ivProduct)
        val tvName = findViewById<TextView>(R.id.tvProductName)
        val tvPrice = findViewById<TextView>(R.id.tvProductPrice)
        val tvPriceBottom = findViewById<TextView>(R.id.tvProductPriceBottom)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnCartTop = findViewById<ImageButton>(R.id.btnCart)
        val btnAddToCart = findViewById<Button>(R.id.btnAddToCart)
        val sizeLayout = findViewById<LinearLayout>(R.id.sizeLayout)
        val galleryLayout = findViewById<LinearLayout>(R.id.galleryLayout)

        btnBack.setOnClickListener { finish() }
        
        btnCartTop.setOnClickListener {
            startActivity(Intent(this, MyCartActivity::class.java))
        }

        // Setup size selection
        setupSizeSelection(sizeLayout)

        val database = FirebaseDatabase.getInstance().getReference("products").child(productId)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    product = snapshot.getValue(Product::class.java)
                    product?.let {
                        tvName.text = it.name ?: "Unknown Shoe"
                        val priceText = "₹${it.price ?: 0.0}"
                        tvPrice.text = priceText
                        tvPriceBottom.text = priceText
                        tvDescription.text = it.description ?: "No description available."
                        
                        val imageUrl = it.imageUrl ?: (if (!it.imageUrls.isNullOrEmpty()) it.imageUrls!![0] else "")
                        
                        Glide.with(this@ProductDetailsActivity)
                            .load(imageUrl)
                            .placeholder(android.R.drawable.ic_menu_report_image)
                            .error(android.R.drawable.ic_menu_report_image)
                            .into(ivProduct)

                        // If product has gallery images, populate them
                        setupGallery(galleryLayout, it, ivProduct)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@ProductDetailsActivity, "Error loading product data", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        btnAddToCart.setOnClickListener {
            if (selectedSize == null) {
                Toast.makeText(this, "Please select a size first", Toast.LENGTH_SHORT).show()
            } else {
                product?.let { addToCart(it) }
            }
        }
    }

    private fun setupSizeSelection(layout: LinearLayout) {
        for (i in 0 until layout.childCount) {
            val view = layout.getChildAt(i)
            if (view is TextView) {
                view.setOnClickListener {
                    // Deselect others
                    for (j in 0 until layout.childCount) {
                        val otherView = layout.getChildAt(j)
                        if (otherView is TextView) {
                            otherView.setBackgroundResource(R.drawable.size_bg_unselected)
                            otherView.setTextColor(ContextCompat.getColor(this, R.color.text_grey))
                        }
                    }
                    // Select this one
                    view.setBackgroundResource(R.drawable.size_bg_selected)
                    view.setTextColor(ContextCompat.getColor(this, R.color.white))
                    selectedSize = view.text.toString().toIntOrNull()
                }
            }
        }
    }

    private fun setupGallery(layout: LinearLayout, product: Product, mainImageView: ImageView) {
        val images = mutableListOf<String>()
        product.imageUrl?.let { images.add(it) }
        product.imageUrls?.let { images.addAll(it) }
        
        if (images.isNotEmpty()) {
            layout.removeAllViews()
            val sizeInPx = (56 * resources.displayMetrics.density).toInt() // Standard thumbnail size (56dp)
            for (url in images.distinct()) {
                val imageView = ImageView(this)
                val params = LinearLayout.LayoutParams(sizeInPx, sizeInPx)
                params.setMargins(0, 0, 12, 0)
                imageView.layoutParams = params
                imageView.setPadding(8, 8, 8, 8)
                imageView.setBackgroundResource(R.drawable.edit_text_bg)
                
                Glide.with(this).load(url).into(imageView)
                
                imageView.setOnClickListener {
                    Glide.with(this@ProductDetailsActivity).load(url).into(mainImageView)
                }
                layout.addView(imageView)
            }
        }
    }

    private fun addToCart(product: Product) {
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("userId", null)

        if (userId != null) {
            val database = FirebaseDatabase.getInstance().getReference("carts").child(userId)
            
            // To allow multiple sizes of the same shoe, we create a unique key
            val cartItemKey = "${product.id}_$selectedSize"
            
            // Create a map or a copy of the product to include the selected size
            val cartItem = HashMap<String, Any>()
            cartItem["id"] = product.id ?: ""
            cartItem["name"] = product.name ?: ""
            cartItem["price"] = product.price ?: 0.0
            cartItem["imageUrl"] = product.imageUrl ?: ""
            cartItem["category"] = product.category ?: ""
            cartItem["selectedSize"] = selectedSize ?: 0
            cartItem["quantity"] = 1

            database.child(cartItemKey).setValue(cartItem)
                .addOnSuccessListener {
                    Toast.makeText(this, "Added to cart (Size: $selectedSize)", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please sign in first", Toast.LENGTH_SHORT).show()
        }
    }
}
