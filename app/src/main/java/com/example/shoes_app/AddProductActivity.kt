package com.example.shoes_app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class AddProductActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDescription: EditText
    private lateinit var ivProductPreview: ImageView
    private lateinit var autoCompleteCategory: AutoCompleteTextView
    private lateinit var btnAdd: Button
    private lateinit var tvTitle: TextView
    
    private var isEditMode = false
    private var productId: String? = null
    private var imageUri: Uri? = null
    private var existingImageUrl: String? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data?.data
            ivProductPreview.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        etName = findViewById(R.id.etProductName)
        etPrice = findViewById(R.id.etProductPrice)
        etDescription = findViewById(R.id.etProductDescription)
        ivProductPreview = findViewById(R.id.ivProductPreview)
        btnAdd = findViewById(R.id.btnAddProduct)
        autoCompleteCategory = findViewById(R.id.autoCompleteCategory)
        tvTitle = findViewById(R.id.tvTitle)

        val categories = arrayOf("Nike", "Adidas", "Puma", "Jordan", "Reebok")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        autoCompleteCategory.setAdapter(adapter)

        isEditMode = intent.getBooleanExtra("isEdit", false)
        productId = intent.getStringExtra("productId")

        if (isEditMode && productId != null) {
            tvTitle.text = "Edit Product"
            btnAdd.text = "Update Product"
            loadProductData(productId!!)
        }

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        ivProductPreview.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImage.launch(intent)
        }

        btnAdd.setOnClickListener {
            validateAndSave()
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
                        autoCompleteCategory.setText(it.category, false)
                        existingImageUrl = it.imageUrl
                        if (!it.imageUrl.isNullOrEmpty()) {
                            Glide.with(this@AddProductActivity).load(it.imageUrl).into(ivProductPreview)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun validateAndSave() {
        val name = etName.text.toString().trim()
        val priceStr = etPrice.text.toString().trim()
        val category = autoCompleteCategory.text.toString().trim()
        val description = etDescription.text.toString().trim()

        if (name.isEmpty() || priceStr.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri == null && existingImageUrl == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceStr.toDoubleOrNull() ?: 0.0
        
        if (imageUri != null) {
            uploadImageAndSave(name, price, category, description)
        } else {
            saveToDatabase(name, price, category, description, existingImageUrl)
        }
    }

    private fun uploadImageAndSave(name: String, price: Double, category: String, description: String) {
        btnAdd.isEnabled = false
        btnAdd.text = "Uploading Image..."
        
        val fileName = "product_${System.currentTimeMillis()}.jpg"
        val storageRef = FirebaseStorage.getInstance().getReference("product_images/$fileName")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveToDatabase(name, price, category, description, uri.toString())
                }
            }
            .addOnFailureListener {
                btnAdd.isEnabled = true
                btnAdd.text = "Error, try again"
                Toast.makeText(this, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveToDatabase(name: String, price: Double, category: String, description: String, imageUrl: String?) {
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
                Toast.makeText(this, "Product Saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                btnAdd.isEnabled = true
                Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show()
            }
    }
}
