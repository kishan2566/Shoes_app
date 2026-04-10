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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavouriteProductAdapter(
    private var products: MutableList<Product>,
    private val context: Context,
    private val userId: String
) : RecyclerView.Adapter<FavouriteProductAdapter.FavViewHolder>() {

    class FavViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProduct: ImageView = view.findViewById(R.id.ivProduct)
        val tvName: TextView = view.findViewById(R.id.tvProductName)
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvPrice: TextView = view.findViewById(R.id.tvProductPrice)
        val btnAddToCart: ImageButton = view.findViewById(R.id.btnAddToCart)
        val btnRemoveFav: ImageButton = view.findViewById(R.id.btnRemoveFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        // Use the new grid-style layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fav_grid, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val product = products[position]

        holder.tvName.text = product.name
        holder.tvCategory.text = product.category ?: "BEST SELLER"
        holder.tvPrice.text = "₹${product.price}"

        val imageUrl = product.imageUrl
            ?: if (!product.imageUrls.isNullOrEmpty()) product.imageUrls!![0] else ""

        loadImage(imageUrl, holder.ivProduct)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("productId", product.id)
            context.startActivity(intent)
        }

        holder.btnRemoveFav.setOnClickListener {
            val productId = product.id ?: return@setOnClickListener
            removeFavourite(productId)
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                products.removeAt(pos)
                notifyItemRemoved(pos)
                Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show()
            }
        }

        holder.btnAddToCart.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("productId", product.id)
            context.startActivity(intent)
        }
    }

    private fun loadImage(url: String, imageView: ImageView) {
        if (url.isEmpty()) {
            imageView.setImageResource(R.mipmap.ic_launcher_foreground)
            return
        }

        if (url.startsWith("http")) {
            Glide.with(context)
                .load(url)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(imageView)
        } else {
            var resId = context.resources.getIdentifier(url, "mipmap", context.packageName)
            if (resId == 0) {
                resId = context.resources.getIdentifier(url, "drawable", context.packageName)
            }

            if (resId != 0) {
                Glide.with(context).load(resId).into(imageView)
            } else {
                imageView.setImageResource(R.mipmap.ic_launcher_foreground)
            }
        }
    }

    private fun removeFavourite(productId: String) {
        val favPrefs = context.getSharedPreferences("Favourites", Context.MODE_PRIVATE)
        val favIds = favPrefs.getStringSet("fav_ids", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        favIds.remove(productId)
        favPrefs.edit().putStringSet("fav_ids", favIds).apply()
    }

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }
}
