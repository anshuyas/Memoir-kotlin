package com.example.memoir.ui.activity


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.memoir.R

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)

        val emailInput = findViewById<EditText>(R.id.email)
        val resetButton = findViewById<Button>(R.id.resetButton)

        resetButton.setOnClickListener {
            val email = emailInput.text.toString()
            if (email.isNotEmpty()) {
                Toast.makeText(this, "Password Reset Link Sent", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
            }
        }
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}