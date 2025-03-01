package com.example.memoir.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.memoir.databinding.ActivityForgotPasswordBinding
import com.example.memoir.viewmodel.UserViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.memoir.repository.UserRepositoryImpl
import com.example.memoir.viewmodel.UserViewModelFactory

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        val userRepository = UserRepositoryImpl()
        val viewModelFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]

        binding.btnResetPassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()

            if (email.isEmpty()) {
                binding.etEmail.error = "Email is required"
                return@setOnClickListener
            }

            userViewModel.forgetPassword(email)

            // Observe LiveData for error messages
            userViewModel.errorMessage.observe(this) { errorMessage ->
                if (errorMessage.isNotEmpty()) {
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            Toast.makeText(this, "Password reset link sent!", Toast.LENGTH_SHORT).show()
        }
    }
}
