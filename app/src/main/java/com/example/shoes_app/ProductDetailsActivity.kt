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
import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide


class ProductDetailsActivity : AppCompatActivity() {
    private var product: Product? = null
    private var selectedSize: Int? = null
    private var isFavourite = false
    private lateinit var btnFavorite: ImageButton
    private var productId: String? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        productId = intent.getStringExtra("productId")
        if (productId == null) {
            Toast.makeText(this, "Product ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userId = sharedPref.getString("userId", null)

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
        btnFavorite = findViewById(R.id.btnFavorite)

        btnBack.setOnClickListener { finish() }

        btnCartTop.setOnClickListener {
            startActivity(Intent(this, MyCartActivity::class.java))
        }

        // Check current favourite state
        checkFavouriteStatus()

        btnFavorite.setOnClickListener {
            toggleFavourite()
        }

        // Setup size selection
        setupSizeSelection(sizeLayout)

        // Load product from local repository
        val foundProduct = ProductRepository.getProductById(productId!!)
        if (foundProduct == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        product = foundProduct
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

            setupGallery(galleryLayout, it, ivProduct)
        }

        btnAddToCart.setOnClickListener {
            if (selectedSize == null) {
                Toast.makeText(this, "Please select a size first", Toast.LENGTH_SHORT).show()
            } else {
                product?.let { addToCart(it) }
            }
        }
    }

    private fun checkFavouriteStatus() {
        val pid = productId ?: return
        val favPrefs = getSharedPreferences("Favourites", Context.MODE_PRIVATE)
        val favIds = favPrefs.getStringSet("fav_ids", emptySet()) ?: emptySet()
        isFavourite = favIds.contains(pid)
        updateFavouriteIcon()
    }

    private fun toggleFavourite() {
        val pid = productId ?: return

        val favPrefs = getSharedPreferences("Favourites", Context.MODE_PRIVATE)
        val favIds = favPrefs.getStringSet("fav_ids", emptySet())?.toMutableSet() ?: mutableSetOf()

        if (isFavourite) {
            favIds.remove(pid)
            favPrefs.edit().putStringSet("fav_ids", favIds).apply()
            isFavourite = false
            updateFavouriteIcon()
            Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show()
        } else {
            favIds.add(pid)
            favPrefs.edit().putStringSet("fav_ids", favIds).apply()
            isFavourite = true
            updateFavouriteIcon()
            Toast.makeText(this, "Added to favourites ❤️", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFavouriteIcon() {
        if (isFavourite) {
            btnFavorite.setImageResource(R.drawable.ic_heart_filled)
            ImageViewCompat.setImageTintList(btnFavorite, null)
        } else {
            btnFavorite.setImageResource(R.drawable.ic_heart)
            ImageViewCompat.setImageTintList(btnFavorite, null)
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
            val sizeInPx = (56 * resources.displayMetrics.density).toInt()
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
        val uid = userId
        if (uid != null) {
            val database = FirebaseDatabase.getInstance().getReference("carts").child(uid)

            // To allow multiple sizes of the same shoe, we create a unique key
            val cartItemKey = "${product.id}_$selectedSize"

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
