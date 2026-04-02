package com.example.shoes_app

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

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
        val btnAddToCart: Button = view.findViewById(R.id.btnAddToCart)
        val btnRemoveFav: ImageButton = view.findViewById(R.id.btnRemoveFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favourite, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val product = products[position]

        holder.tvName.text = product.name
        holder.tvCategory.text = product.category
        holder.tvPrice.text = "₹${product.price}"

        val imageUrl = product.imageUrl
            ?: if (!product.imageUrls.isNullOrEmpty()) product.imageUrls!![0] else ""

        Glide.with(context)
            .load(imageUrl)
            .placeholder(android.R.drawable.ic_menu_report_image)
            .into(holder.ivProduct)

        // Open product details on item click
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("productId", product.id)
            context.startActivity(intent)
        }

        // Remove from favourites
        holder.btnRemoveFav.setOnClickListener {
            val productId = product.id ?: return@setOnClickListener
            FirebaseDatabase.getInstance()
                .getReference("favorites")
                .child(userId)
                .child(productId)
                .removeValue()
                .addOnSuccessListener {
                    val pos = holder.adapterPosition
                    if (pos != RecyclerView.NO_ID.toInt()) {
                        products.removeAt(pos)
                        notifyItemRemoved(pos)
                        Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to remove", Toast.LENGTH_SHORT).show()
                }
        }

        // Add to cart (without size selection — quick add)
        holder.btnAddToCart.setOnClickListener {
            addToCart(product)
        }
    }

    private fun addToCart(product: Product) {
        val database = FirebaseDatabase.getInstance().getReference("carts").child(userId)
        val cartItemId = product.id ?: return

        val cartItem = HashMap<String, Any>()
        cartItem["id"] = product.id ?: ""
        cartItem["name"] = product.name ?: ""
        cartItem["price"] = product.price ?: 0.0
        cartItem["imageUrl"] = product.imageUrl ?: ""
        cartItem["category"] = product.category ?: ""
        cartItem["quantity"] = 1

        database.child(cartItemId).setValue(cartItem)
            .addOnSuccessListener {
                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to add to cart", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }
}
