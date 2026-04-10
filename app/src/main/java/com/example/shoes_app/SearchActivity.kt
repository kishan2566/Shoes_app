package com.example.shoes_app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class SearchActivity : AppCompatActivity() {
    private lateinit var etSearch: EditText
    private lateinit var rvResults: RecyclerView
    private lateinit var tvNoResults: TextView
    private val allProducts = mutableListOf<Product>()
    private val filteredList = mutableListOf<Product>()
    private lateinit var adapter: SearchProductAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        database = FirebaseDatabase.getInstance().getReference("products")

        etSearch = findViewById(R.id.etSearch)
        rvResults = findViewById(R.id.rvSearchResults)
        tvNoResults = findViewById(R.id.tvNoResults)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Initialize RecyclerView
        rvResults.layoutManager = LinearLayoutManager(this)
        adapter = SearchProductAdapter(filteredList, this)
        rvResults.adapter = adapter

        fetchProductsFromFirebase()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun fetchProductsFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allProducts.clear()
                for (postSnapshot in snapshot.children) {
                    val product = postSnapshot.getValue(Product::class.java)
                    product?.let {
                        it.id = postSnapshot.key
                        allProducts.add(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SearchActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filter(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            rvResults.visibility = View.GONE
            tvNoResults.visibility = View.GONE
        } else {
            val results = allProducts.filter { 
                it.name?.contains(query, ignoreCase = true) == true || 
                it.category?.contains(query, ignoreCase = true) == true 
            }
            filteredList.addAll(results)
            rvResults.visibility = if (results.isNotEmpty()) View.VISIBLE else View.GONE
            tvNoResults.visibility = if (results.isEmpty()) View.VISIBLE else View.GONE
        }
        adapter.notifyDataSetChanged()
    }
}
