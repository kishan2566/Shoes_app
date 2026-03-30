package com.example.shoes_app

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class AddProductActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDescription: EditText
    private lateinit var etImageUrl: EditText
    private lateinit var autoCompleteCategory: AutoCompleteTextView
    private lateinit var btnAdd: Button
    private lateinit var tvTitle: TextView
    
    private var isEditMode = false
    private var productId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        etName = findViewById(R.id.etProductName)
        etPrice = findViewById(R.id.etProductPrice)
        etDescription = findViewById(R.id.etProductDescription)
        etImageUrl = findViewById(R.id.etProductImageUrl)
        btnAdd = findViewById(R.id.btnAddProduct)
        autoCompleteCategory = findViewById(R.id.autoCompleteCategory)
        tvTitle = findViewById(R.id.tvTitle)

        // Setup Category Dropdown
        val categories = arrayOf("Nike", "Adidas", "Puma", "Under Armour", "Jordan", "Reebok")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        autoCompleteCategory.setAdapter(adapter)

        // Check if we are in Edit Mode
        isEditMode = intent.getBooleanExtra("isEdit", false)
        productId = intent.getStringExtra("productId")

        if (isEditMode && productId != null) {
            tvTitle.text = "Edit Product"
            btnAdd.text = "Update Product"
            loadProductData(productId!!)
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        btnAdd.setOnClickListener {
            val name = etName.text.toString().trim()
            val priceStr = etPrice.text.toString().trim()
            val category = autoCompleteCategory.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val imageUrl = etImageUrl.text.toString().trim()

            if (name.isNotEmpty() && priceStr.isNotEmpty() && category.isNotEmpty() && imageUrl.isNotEmpty()) {
                val price = priceStr.toDoubleOrNull() ?: 0.0
                saveProductToDatabase(name, price, category, description, imageUrl)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadProductData(id: String) {
        FirebaseDatabase.getInstance().getReference("products").child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val product = snapshot.getValue(Product::class.java)
                    product?.let {
                        etName.setText(it.name)
                        etPrice.setText(it.price.toString())
                        etDescription.setText(it.description)
                        etImageUrl.setText(it.imageUrl)
                        autoCompleteCategory.setText(it.category, false)
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun saveProductToDatabase(name: String, price: Double, category: String, description: String, imageUrl: String) {
        btnAdd.isEnabled = false
        btnAdd.text = if (isEditMode) "Updating..." else "Adding..."
        
        val database = FirebaseDatabase.getInstance().getReference("products")
        val targetId = productId ?: database.push().key ?: return
        
        val product = Product(
            id = targetId,
            name = name,
            brand = category,
            price = price,
            category = category,
            description = description,
            imageUrl = imageUrl,
            discount = 0,
            stockQuantity = 10,
            rating = 4.5
        )
        
        database.child(targetId).setValue(product)
            .addOnSuccessListener {
                val msg = if (isEditMode) "Product updated" else "Product added"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                btnAdd.isEnabled = true
                btnAdd.text = if (isEditMode) "Update Product" else "Add Product"
                Toast.makeText(this, "Operation failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}