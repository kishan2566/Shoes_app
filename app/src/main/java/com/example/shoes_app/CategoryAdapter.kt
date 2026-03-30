package com.example.shoes_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private val categories: List<String>,
    private var selectedPosition: Int = 0,
    private val onCategoryClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llCategory: LinearLayout = view.findViewById(R.id.llCategory)
        val tvCategoryName: TextView = view.findViewById(R.id.tvCategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.tvCategoryName.text = category

        if (selectedPosition == position) {
            holder.llCategory.setBackgroundResource(R.drawable.category_bg_selected)
            holder.tvCategoryName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        } else {
            holder.llCategory.setBackgroundResource(R.drawable.category_bg_unselected)
            holder.tvCategoryName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.text_dark))
        }

        holder.itemView.setOnClickListener {
            val oldPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(oldPosition)
            notifyItemChanged(selectedPosition)
            onCategoryClick(category)
        }
    }

    override fun getItemCount() = categories.size
}