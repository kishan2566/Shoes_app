package com.example.shoes_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter(
    private var orders: List<Order>,
    private val onUpdateStatusClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvOrderId: TextView = view.findViewById(R.id.tvOrderId)
        val tvStatus: TextView = view.findViewById(R.id.tvOrderStatus)
        val tvDate: TextView = view.findViewById(R.id.tvOrderDate)
        val tvTotal: TextView = view.findViewById(R.id.tvOrderTotal)
        val btnUpdate: Button = view.findViewById(R.id.btnUpdateStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.tvOrderId.text = "Order #${order.orderId?.takeLast(6)}"
        holder.tvStatus.text = order.status
        holder.tvTotal.text = "Total: $${String.format("%.2f", order.totalPrice ?: 0.0)}"
        
        val date = Date(order.timestamp ?: 0)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        holder.tvDate.text = "Date: ${format.format(date)}"

        if (order.status == "Delivered") {
            holder.btnUpdate.visibility = View.GONE
        } else {
            holder.btnUpdate.visibility = View.VISIBLE
            holder.btnUpdate.setOnClickListener { onUpdateStatusClick(order) }
        }
    }

    override fun getItemCount() = orders.size

    fun updateOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}