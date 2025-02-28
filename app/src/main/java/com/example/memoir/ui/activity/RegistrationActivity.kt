package com.example.memoir.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.memoir.R
import com.example.memoir.viewmodel.UserViewModel

class RegistrationActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)

        val emailInput = findViewById<EditText>(R.id.email)
        val passwordInput = findViewById<EditText>(R.id.password)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.length >= 6) { // Basic validation
                registerUser(email, password)
            } else {
                Toast.makeText(this, "Enter valid email and password (6+ chars)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        userViewModel.signup(email, password) { success, userId, message ->
            if (success) {
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
