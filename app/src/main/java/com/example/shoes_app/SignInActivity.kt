package com.example.shoes_app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignInActivity : AppCompatActivity() {
    private var isPasswordVisible = false
    private var isLoading = true
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // Keep the splash screen on screen until we check the login status
        splashScreen.setKeepOnScreenCondition { isLoading }

        // Check login status
        val savedUserId = sharedPref.getString("userId", null)
        
        if (savedUserId != null) {
            checkUserStatusAndRedirect(savedUserId)
        } else {
            // No saved user, we can stop showing splash and show login
            isLoading = false
            setupLoginUI()
        }
    }

    private fun checkUserStatusAndRedirect(userId: String) {
        val database = FirebaseDatabase.getInstance()
        database.getReference("users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        val intent = if (user.isAdmin || user.email == "admin@gmail.com") {
                            Intent(this@SignInActivity, AdminDashboardActivity::class.java)
                        } else {
                            Intent(this@SignInActivity, HomeActivity::class.java)
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        // User data not found, show login
                        isLoading = false
                        setupLoginUI()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    isLoading = false
                    setupLoginUI()
                }
            })
    }

    private fun setupLoginUI() {
        setContentView(R.layout.activity_sign_in)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val ivPasswordToggle = findViewById<ImageView>(R.id.ivPasswordToggle)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val tvRecovery = findViewById<TextView>(R.id.tvRecovery)
        val tvFooter = findViewById<TextView>(R.id.tvFooter)

        ivPasswordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivPasswordToggle.alpha = 1.0f
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivPasswordToggle.alpha = 0.5f
            }
            etPassword.setSelection(etPassword.text.length)
        }

        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString().trim().lowercase()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnSignIn.isEnabled = false
            btnSignIn.text = "Signing in..."

            val database = FirebaseDatabase.getInstance()
            val usersRef = database.getReference("users")

            usersRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        btnSignIn.isEnabled = true
                        btnSignIn.text = "Sign In"
                        
                        if (snapshot.exists()) {
                            var loggedIn = false
                            for (userSnap in snapshot.children) {
                                val user = userSnap.getValue(User::class.java)
                                if (user?.password == password) {
                                    val userId = userSnap.key
                                    sharedPref.edit().putString("userId", userId).apply()

                                    val intent = if (user?.isAdmin == true || email == "admin@gmail.com") {
                                        Intent(this@SignInActivity, AdminDashboardActivity::class.java)
                                    } else {
                                        Intent(this@SignInActivity, HomeActivity::class.java)
                                    }
                                    startActivity(intent)
                                    finish()
                                    loggedIn = true
                                    break
                                }
                            }
                            if (!loggedIn) {
                                Toast.makeText(this@SignInActivity, "Invalid password", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@SignInActivity, "No account found", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        btnSignIn.isEnabled = true
                        btnSignIn.text = "Sign In"
                        Toast.makeText(this@SignInActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        tvRecovery.setOnClickListener {
            startActivity(Intent(this, RecoveryPasswordActivity::class.java))
        }

        tvFooter.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}