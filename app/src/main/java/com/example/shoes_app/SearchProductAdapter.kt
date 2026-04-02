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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchProductAdapter(
    private var products: List<Product>,
    private val context: Context
) : RecyclerView.Adapter<SearchProductAdapter.ViewHolder>() {

    private val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    private val userId: String? get() = sharedPref.getString("userId", null)
    private val favouritedIds = mutableSetOf<String>()

    init {
        loadFavouriteIds()
    }

    private fun loadFavouriteIds() {
        val uid = userId ?: return
        FirebaseDatabase.getInstance()
            .getReference("favorites")
            .child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    favouritedIds.clear()
                    for (snap in snapshot.children) {
                        snap.key?.let { favouritedIds.add(it) }
                    }
                    notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

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
            btn.clearColorFilter()
        } else {
            btn.setImageResource(R.drawable.ic_heart)
            btn.setColorFilter(ContextCompat.getColor(context, R.color.text_grey))
        }
    }

    private fun toggleFavourite(product: Product, btn: ImageButton) {
        val uid = userId
        if (uid == null) {
            Toast.makeText(context, "Please sign in first", Toast.LENGTH_SHORT).show()
            return
        }
        val pid = product.id ?: return
        val favRef = FirebaseDatabase.getInstance()
            .getReference("favorites")
            .child(uid)
            .child(pid)

        if (favouritedIds.contains(pid)) {
            favRef.removeValue()
                .addOnSuccessListener {
                    favouritedIds.remove(pid)
                    updateFavouriteIcon(btn, pid)
                    Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show()
                }
        } else {
            val favItem = HashMap<String, Any>()
            favItem["id"] = product.id ?: ""
            favItem["name"] = product.name ?: ""
            favItem["price"] = product.price ?: 0.0
            favItem["imageUrl"] = product.imageUrl ?: ""
            favItem["category"] = product.category ?: ""
            favItem["description"] = product.description ?: ""
            favItem["brand"] = product.brand ?: ""

            favRef.setValue(favItem)
                .addOnSuccessListener {
                    favouritedIds.add(pid)
                    updateFavouriteIcon(btn, pid)
                    Toast.makeText(context, "Added to favourites ❤️", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun getItemCount() = products.size
}