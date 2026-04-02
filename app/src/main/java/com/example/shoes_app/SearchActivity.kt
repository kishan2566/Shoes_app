package com.example.shoes_app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SearchActivity : AppCompatActivity() {
    private lateinit var etSearch: EditText
    private lateinit var rvResults: RecyclerView
    private lateinit var tvNoResults: TextView
    private val allProducts = mutableListOf<Product>()
    private val filteredList = mutableListOf<Product>()
    private lateinit var adapter: SearchProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

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

        fetchProducts()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun fetchProducts() {
        allProducts.clear()
        allProducts.addAll(ProductRepository.products)
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