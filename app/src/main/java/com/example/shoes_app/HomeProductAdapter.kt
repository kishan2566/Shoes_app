package com.example.shoes_app

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import android.animation.AnimatorSet
import android.animation.ObjectAnimator

class HomeProductAdapter(
    private var products: List<Product>,
    private val context: Context
) : RecyclerView.Adapter<HomeProductAdapter.ProductViewHolder>() {

    private val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    private val userId: String? get() = sharedPref.getString("userId", null)

    // Favourite IDs backed by SharedPreferences — no Firebase reads needed
    private val favPrefs = context.getSharedPreferences("Favourites", Context.MODE_PRIVATE)
    private val favouritedIds: MutableSet<String>
        get() = favPrefs.getStringSet("fav_ids", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProduct: ImageView = view.findViewById(R.id.ivProduct)
        val tvName: TextView = view.findViewById(R.id.tvProductName)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val btnAddToCart: ImageButton = view.findViewById(R.id.btnAddToCart)
        val btnFavorite: ImageButton = view.findViewById(R.id.btnFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.tvName.text = product.name
        holder.tvCategory.text = product.category
        holder.tvPrice.text = "₹${product.price}"

        val imageUrl = product.imageUrl ?: (if (!product.imageUrls.isNullOrEmpty()) product.imageUrls!![0] else "")

        Glide.with(context)
            .load(imageUrl)
            .placeholder(android.R.drawable.ic_menu_report_image)
            .into(holder.ivProduct)

        // Update heart icon based on current favourite state
        updateFavouriteIcon(holder.btnFavorite, product.id)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("productId", product.id)
            context.startActivity(intent)
        }

        holder.btnAddToCart.setOnClickListener {
            addToCart(product)
        }

        holder.btnFavorite.setOnClickListener {
            toggleFavourite(product, holder.btnFavorite)
        }
    }

    private fun updateFavouriteIcon(btn: ImageButton, productId: String?) {
        if (productId != null && favouritedIds.contains(productId)) {
            // Red filled heart — clear any tint so the #FF4B4B color shows
            btn.setImageResource(R.drawable.ic_heart_filled)
            ImageViewCompat.setImageTintList(btn, null)
        } else {
            // Grey outline heart
            btn.setImageResource(R.drawable.ic_heart)
            ImageViewCompat.setImageTintList(btn, null)
        }
    }

    private fun toggleFavourite(product: Product, btn: ImageButton) {
        val pid = product.id ?: return
        val currentIds = favouritedIds  // fresh copy each time

        if (currentIds.contains(pid)) {
            // Remove
            currentIds.remove(pid)
            favPrefs.edit().putStringSet("fav_ids", currentIds).apply()
            updateFavouriteIcon(btn, pid)
            animateHeart(btn)
            Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show()
        } else {
            // Add
            currentIds.add(pid)
            favPrefs.edit().putStringSet("fav_ids", currentIds).apply()
            updateFavouriteIcon(btn, pid)
            animateHeart(btn)
            Toast.makeText(context, "Added to favourites ❤️", Toast.LENGTH_SHORT).show()
        }
    }

    private fun animateHeart(btn: ImageButton) {
        val scaleUpX = ObjectAnimator.ofFloat(btn, "scaleX", 1f, 1.4f)
        val scaleUpY = ObjectAnimator.ofFloat(btn, "scaleY", 1f, 1.4f)
        val scaleDownX = ObjectAnimator.ofFloat(btn, "scaleX", 1.4f, 1f)
        val scaleDownY = ObjectAnimator.ofFloat(btn, "scaleY", 1.4f, 1f)
        scaleUpX.duration = 120
        scaleUpY.duration = 120
        scaleDownX.duration = 120
        scaleDownY.duration = 120
        AnimatorSet().apply {
            play(scaleUpX).with(scaleUpY)
            play(scaleDownX).with(scaleDownY).after(scaleUpX)
            start()
        }
    }

    private fun addToCart(product: Product) {
        val uid = userId
        if (uid != null) {
            val database = FirebaseDatabase.getInstance().getReference("carts").child(uid)
            val cartItemId = product.id ?: return

            database.child(cartItemId).setValue(product)
                .addOnSuccessListener {
                    Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to add to cart", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Please sign in first", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}