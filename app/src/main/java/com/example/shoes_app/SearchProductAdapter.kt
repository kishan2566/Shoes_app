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
import android.animation.AnimatorSet
import android.animation.ObjectAnimator

class SearchProductAdapter(
    private var products: List<Product>,
    private val context: Context
) : RecyclerView.Adapter<SearchProductAdapter.ViewHolder>() {

    // Favourites backed by SharedPreferences — no Firebase reads needed
    private val favPrefs = context.getSharedPreferences("Favourites", Context.MODE_PRIVATE)
    private val favouritedIds: MutableSet<String>
        get() = favPrefs.getStringSet("fav_ids", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProduct: ImageView = view.findViewById(R.id.ivProduct)
        val tvName: TextView = view.findViewById(R.id.tvProductName)
        val tvPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val btnFavorite: ImageButton = view.findViewById(R.id.btnFavorite)
        val btnAddToCart: ImageButton = view.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.tvName.text = product.name
        holder.tvPrice.text = "₹${product.price}"
        holder.tvCategory.text = product.category

        val imageUrl = product.imageUrl ?: (if (!product.imageUrls.isNullOrEmpty()) product.imageUrls!![0] else "")
        Glide.with(context)
            .load(imageUrl)
            .placeholder(android.R.drawable.ic_menu_report_image)
            .into(holder.ivProduct)

        // Update heart icon state
        updateFavouriteIcon(holder.btnFavorite, product.id)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("productId", product.id)
            context.startActivity(intent)
        }

        holder.btnFavorite.setOnClickListener {
            toggleFavourite(product, holder.btnFavorite)
        }

        holder.btnAddToCart.setOnClickListener {
            // Navigate to product details so user can pick a size
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("productId", product.id)
            context.startActivity(intent)
        }
    }

    private fun updateFavouriteIcon(btn: ImageButton, productId: String?) {
        if (productId != null && favouritedIds.contains(productId)) {
            btn.setImageResource(R.drawable.ic_heart_filled)
            ImageViewCompat.setImageTintList(btn, null)
        } else {
            btn.setImageResource(R.drawable.ic_heart)
            ImageViewCompat.setImageTintList(btn, null)
        }
    }

    private fun toggleFavourite(product: Product, btn: ImageButton) {
        val pid = product.id ?: return
        val currentIds = favouritedIds

        if (currentIds.contains(pid)) {
            currentIds.remove(pid)
            favPrefs.edit().putStringSet("fav_ids", currentIds).apply()
            updateFavouriteIcon(btn, pid)
            animateHeart(btn)
            Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show()
        } else {
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

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}