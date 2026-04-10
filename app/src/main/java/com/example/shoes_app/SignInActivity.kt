package com.example.shoes_app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignInActivity : AppCompatActivity() {
    private var isPasswordVisible = false
    private lateinit var sharedPref: SharedPreferences
    private lateinit var splashProgressBar: ProgressBar
    private lateinit var loginScrollView: View
    private lateinit var splashContainer: View
    private var isTaskCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Initialize SplashScreen (MUST be before super.onCreate)
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // 2. Set the content view
        setContentView(R.layout.activity_sign_in)

        // 3. Initialize Views
        sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        splashProgressBar = findViewById(R.id.splashProgressBar)
        loginScrollView = findViewById(R.id.loginScrollView)
        splashContainer = findViewById(R.id.splashContainer)

        // Ensure custom splash is visible and login is hidden
        splashContainer.visibility = View.VISIBLE
        loginScrollView.visibility = View.GONE

        // 4. Start Auth Check
        checkAuthAndNavigate()
    }

    private fun checkAuthAndNavigate() {
        val savedUserId = sharedPref.getString("userId", null)
        
        if (!savedUserId.isNullOrEmpty()) {
            // Check Firebase status
            val database = FirebaseDatabase.getInstance().getReference("users").child(savedUserId)
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isFinishing || isDestroyed) return
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        isTaskCompleted = true
                        navigateToDashboard(user)
                    } else {
                        proceedToLogin()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    proceedToLogin()
                }
            })
            
            // Failsafe: timeout after 5 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                if (!isTaskCompleted && !isFinishing) {
                    proceedToLogin()
                }
            }, 5000)
            
        } else {
            // No saved user, show splash for 1.5s then show login
            Handler(Looper.getMainLooper()).postDelayed({
                proceedToLogin()
            }, 1500)
        }
    }

    private fun navigateToDashboard(user: User) {
        val intent = if (user.isAdmin || user.email == "admin@gmail.com") {
            Intent(this, AdminDashboardActivity::class.java)
        } else {
            Intent(this, HomeActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    private fun proceedToLogin() {
        if (isFinishing || isDestroyed) return
        isTaskCompleted = true
        splashContainer.visibility = View.GONE
        loginScrollView.visibility = View.VISIBLE
        setupLoginListeners()
    }

    private fun setupLoginListeners() {
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

            setLoadingState(true)

            FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var loggedIn = false
                            for (userSnap in snapshot.children) {
                                val user = userSnap.getValue(User::class.java)
                                if (user?.password == password) {
                                    sharedPref.edit().putString("userId", userSnap.key).apply()
                                    if (user != null) navigateToDashboard(user)
                                    loggedIn = true
                                    break
                                }
                            }
                            if (!loggedIn) {
                                setLoadingState(false)
                                Toast.makeText(this@SignInActivity, "Invalid password", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            setLoadingState(false)
                            Toast.makeText(this@SignInActivity, "No account found", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        setLoadingState(false)
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

    private fun setLoadingState(loading: Boolean) {
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        if (loading) {
            btnSignIn.isEnabled = false
            btnSignIn.text = "Signing in..."
        } else {
            btnSignIn.isEnabled = true
            btnSignIn.text = "Sign In"
        }
    }
}