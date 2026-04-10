package com.example.shoes_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.google.firebase.database.FirebaseDatabase

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

        val ivProduct = findViewById<PhotoView>(R.id.ivProduct)
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

        checkFavouriteStatus()

        btnFavorite.setOnClickListener {
            toggleFavourite()
        }

        setupSizeSelection(sizeLayout)

        // Fetch product details - checking manually first for IDs 1-6
        loadProductDetails(ivProduct, tvName, tvPrice, tvPriceBottom, tvDescription, galleryLayout)

        btnAddToCart.setOnClickListener {
            if (selectedSize == null) {
                Toast.makeText(this, "Please select a size first", Toast.LENGTH_SHORT).show()
            } else {
                product?.let { addToCart(it) }
            }
        }
    }

    private fun loadProductDetails(
        ivProduct: PhotoView, tvName: TextView, tvPrice: TextView,
        tvPriceBottom: TextView, tvDescription: TextView,
        galleryLayout: LinearLayout
    ) {
        val pid = productId ?: return
        
        // Check if it's one of our manual products (1-6)
        // Providing the correct image resource names for each manual product
        val manualProduct = when (pid) {
            "1" -> Product("1", "Nike Air Jordan 1", "Nike", 12000.0, "Nike", "The iconic Air Jordan 1 high top sneaker. A classic for basketball and street style.", "ic_launcher_foreground", listOf("ic_launcher_foreground"), 0, 4.8, 15)
            "2" -> Product("2", "Adidas Ultraboost", "Adidas", 15000.0, "Adidas", "Experience the ultimate comfort and energy return with Adidas Ultraboost running shoes.", "shoes1", listOf("shoes1"), 0, 4.7, 20)
            "3" -> Product("3", "Puma RS-X", "Puma", 8000.0, "Puma", "Bold, bulky and colorful. The Puma RS-X reimagines street style with its retro-futuristic design.", "shoes2", listOf("shoes2"), 0, 4.5, 10)
            "4" -> Product("4", "Jordan Retro 4", "Jordan", 18000.0, "Jordan", "One of the most popular Jordan models of all time, the Retro 4 features side ankle supports and innovative mesh.", "shoes3", listOf("shoes3"), 0, 4.9, 5)
            "5" -> Product("5", "Nike Air Max 270", "Nike", 13500.0, "Nike", "The first lifestyle Air Max from Nike brings you style, comfort and a big attitude.", "shoes4", listOf("shoes4"), 0, 4.9, 8)
            "6" -> Product("6", "Adidas NMD_R1", "Adidas", 14000.0, "Adidas", "A fusion of street style and performance technology. The Adidas NMD_R1 is made for the urban nomad.", "ic_launcher_foreground", listOf("ic_launcher_foreground"), 0, 4.4, 25)
            else -> null
        }

        if (manualProduct != null) {
            displayProduct(manualProduct, ivProduct, tvName, tvPrice, tvPriceBottom, tvDescription, galleryLayout)
        } else {
            // Otherwise fetch from Firebase
            FirebaseDatabase.getInstance().getReference("products").child(pid)
                .get().addOnSuccessListener { snapshot ->
                    val p = snapshot.getValue(Product::class.java)
                    p?.let { 
                        displayProduct(it, ivProduct, tvName, tvPrice, tvPriceBottom, tvDescription, galleryLayout)
                    }
                }
        }
    }

    private fun displayProduct(
        p: Product, ivProduct: PhotoView, tvName: TextView, tvPrice: TextView,
        tvPriceBottom: TextView, tvDescription: TextView,
        galleryLayout: LinearLayout
    ) {
        product = p
        tvName.text = p.name ?: "Unknown Shoe"
        val priceText = "₹${p.price ?: 0.0}"
        tvPrice.text = priceText
        tvPriceBottom.text = priceText
        tvDescription.text = p.description ?: "No description available."

        val imageUrl = p.imageUrl ?: (if (!p.imageUrls.isNullOrEmpty()) p.imageUrls!![0] else "")
        loadImage(imageUrl, ivProduct)

        setupGallery(galleryLayout, p, ivProduct)
    }

    private fun loadImage(url: String, imageView: ImageView) {
        if (url.isEmpty()) {
            imageView.setImageResource(R.mipmap.ic_launcher_foreground)
            return
        }

        if (url.startsWith("http")) {
            // It's a URL (from Firebase)
            Glide.with(this)
                .load(url)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(imageView)
        } else {
            // It's a resource name (for manual products)
            var resId = resources.getIdentifier(url, "mipmap", packageName)
            if (resId == 0) {
                resId = resources.getIdentifier(url, "drawable", packageName)
            }

            if (resId != 0) {
                Glide.with(this)
                    .load(resId)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.mipmap.ic_launcher_foreground)
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
            isFavourite = false
            Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show()
        } else {
            favIds.add(pid)
            isFavourite = true
            Toast.makeText(this, "Added to favourites ❤️", Toast.LENGTH_SHORT).show()
        }
        favPrefs.edit().putStringSet("fav_ids", favIds).apply()
        updateFavouriteIcon()
    }

    private fun updateFavouriteIcon() {
        if (isFavourite) {
            btnFavorite.setImageResource(R.drawable.ic_heart_filled)
        } else {
            btnFavorite.setImageResource(R.drawable.ic_heart)
        }
        ImageViewCompat.setImageTintList(btnFavorite, null)
    }

    private fun setupSizeSelection(layout: LinearLayout) {
        for (i in 0 until layout.childCount) {
            val view = layout.getChildAt(i)
            if (view is TextView) {
                view.setOnClickListener {
                    // Reset all backgrounds
                    for (j in 0 until layout.childCount) {
                        val otherView = layout.getChildAt(j)
                        if (otherView is TextView) {
                            otherView.setBackgroundResource(R.drawable.size_bg_unselected)
                            otherView.setTextColor(ContextCompat.getColor(this, R.color.text_grey))
                        }
                    }
                    // Select this size
                    view.setBackgroundResource(R.drawable.size_bg_selected)
                    view.setTextColor(ContextCompat.getColor(this, R.color.white))
                    selectedSize = view.text.toString().toIntOrNull()
                }
            }
        }
    }

    private fun setupGallery(layout: LinearLayout, product: Product, mainImageView: PhotoView) {
        val images = mutableListOf<String>()
        product.imageUrl?.let { if (it.isNotEmpty()) images.add(it) }
        product.imageUrls?.let { images.addAll(it.filter { url -> url.isNotEmpty() }) }
        val distinctImages = images.distinct()

        if (distinctImages.isNotEmpty()) {
            layout.removeAllViews()
            val sizeInPx = (56 * resources.displayMetrics.density).toInt()
            for (url in distinctImages) {
                val imageView = ImageView(this)
                val params = LinearLayout.LayoutParams(sizeInPx, sizeInPx)
                params.setMargins(0, 0, 12, 0)
                imageView.layoutParams = params
                imageView.setPadding(8, 8, 8, 8)
                imageView.setBackgroundResource(R.drawable.edit_text_bg)
                imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE

                loadImage(url, imageView)

                imageView.setOnClickListener {
                    loadImage(url, mainImageView)
                }
                layout.addView(imageView)
            }
        }
    }

    private fun addToCart(product: Product) {
        val uid = userId
        if (uid != null) {
            val database = FirebaseDatabase.getInstance().getReference("carts").child(uid)
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
