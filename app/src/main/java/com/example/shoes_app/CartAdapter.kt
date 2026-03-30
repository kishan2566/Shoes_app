package com.example.shoes_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(
    private var cartItems: List<Product>,
    private val context: Context,
    private val onDeleteClick: (Product) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivItem: ImageView = view.findViewById(R.id.ivItem)
        val tvName: TextView = view.findViewById(R.id.tvItemName)
        val tvPrice: TextView = view.findViewById(R.id.tvItemPrice)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = cartItems[position]
        holder.tvName.text = product.name
        holder.tvPrice.text = "$${product.price}"
        
        Glide.with(context)
            .load(product.imageUrl)
            .placeholder(android.R.drawable.ic_menu_report_image)
            .into(holder.ivItem)

        holder.btnDelete.setOnClickListener { onDeleteClick(product) }
    }

    override fun getItemCount() = cartItems.size

    fun updateItems(newItems: List<Product>) {
        cartItems = newItems
        notifyDataSetChanged()
    }
}