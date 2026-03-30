package com.example.shoes_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class ManageProductsActivity : AppCompatActivity() {
    private lateinit var adapter: ProductAdapter
    private lateinit var database: DatabaseReference
    private val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_products)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        val recyclerView = findViewById<RecyclerView>(R.id.rvProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ProductAdapter(productList, 
            onDeleteClick = { product ->
                deleteProduct(product)
            },
            onEditClick = { product ->
                val intent = Intent(this, AddProductActivity::class.java)
                intent.putExtra("productId", product.id)
                intent.putExtra("isEdit", true)
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAddProduct).setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        database = FirebaseDatabase.getInstance().getReference("products")
        fetchProducts()
    }

    private fun fetchProducts() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    if (product != null) {
                        productList.add(product)
                    }
                }
                adapter.updateProducts(productList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ManageProductsActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteProduct(product: Product) {
        product.id?.let { id ->
            database.child(id).removeValue().addOnSuccessListener {
                Toast.makeText(this, "Product removed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}