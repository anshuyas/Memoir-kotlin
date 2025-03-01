package com.example.memoir.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.memoir.repository.UserRepositoryImpl

/**
 * Factory class for creating instances of [UserViewModel] with a [UserRepositoryImpl] dependency.
 */
class UserViewModelFactory(
    private val repo: UserRepositoryImpl
) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the specified [ViewModel] class.
     *
     * @param modelClass The class of the [ViewModel] to create.
     * @return A new instance of the specified [ViewModel] class.
     * @throws IllegalArgumentException If the [ViewModel] class is unknown.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}