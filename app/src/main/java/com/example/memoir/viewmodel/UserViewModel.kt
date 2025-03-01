package com.example.memoir.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memoir.repository.UserRepositoryImpl
import com.example.memoir.utils.SingleLiveEvent

class UserViewModel(private val repo: UserRepositoryImpl) : ViewModel() {

    // LiveData for loading state
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    // LiveData for login success/failure (one-time event)
    private val _isLoginSuccessful = SingleLiveEvent<Boolean>()
    val isLoginSuccessful: LiveData<Boolean> get() = _isLoginSuccessful

    // LiveData for registration success/failure (one-time event)
    private val _isRegistrationSuccessful = SingleLiveEvent<Boolean>()
    val isRegistrationSuccessful: LiveData<Boolean> get() = _isRegistrationSuccessful

    // LiveData for password reset success/failure (one-time event)
    private val _isPasswordResetSuccessful = SingleLiveEvent<Boolean>()
    val isPasswordResetSuccessful: LiveData<Boolean> get() = _isPasswordResetSuccessful

    // LiveData for error messages (one-time event)
    private val _errorMessage = SingleLiveEvent<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    /**
     * Resets the error message to an empty string.
     */
    private fun resetError() {
        _errorMessage.postValue("")
    }

    /**
     * Attempts to log in the user with the provided email and password.
     */
    fun login(email: String, password: String) {
        _loading.postValue(true)
        resetError()

        repo.login(email, password) { success, message ->
            _loading.postValue(false)
            if (success) {
                _isLoginSuccessful.postValue(true)
            } else {
                _isLoginSuccessful.postValue(false)
                _errorMessage.postValue(message)
            }
        }
    }

    /**
     * Attempts to register a new user with the provided email and password.
     */
    fun signup(email: String, password: String) {
        _loading.postValue(true)
        resetError()

        repo.signup(email, password) { success, _, message ->
            _loading.postValue(false)
            if (success) {
                _isRegistrationSuccessful.postValue(true)
            } else {
                _isRegistrationSuccessful.postValue(false)
                _errorMessage.postValue(message)
            }
        }
    }

    /**
     * Attempts to send a password reset email to the provided email address.
     */
    fun forgetPassword(email: String) {
        _loading.postValue(true)
        resetError()

        repo.forgetPassword(email) { success, message ->
            _loading.postValue(false)
            if (success) {
                _isPasswordResetSuccessful.postValue(true)
            } else {
                _isPasswordResetSuccessful.postValue(false)
                _errorMessage.postValue(message)
            }
        }
    }
}