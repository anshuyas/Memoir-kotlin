package com.example.memoir.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memoir.repository.UserRepositoryImpl

class UserViewModel(private val repo: UserRepositoryImpl) : ViewModel() {

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> = _loginStatus

    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> = _registrationStatus

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun login(email: String, password: String) {
        repo.login(email, password) { success, message ->
            _loginStatus.postValue(success)
            if (!success) _errorMessage.postValue(message)
        }
    }

    fun signup(email: String, password: String) {
        repo.signup(email, password) { success, userId, message ->
            _registrationStatus.postValue(success)
            if (!success) _errorMessage.postValue(message)
        }
    }

    fun forgetPassword(email: String) {
        repo.forgetPassword(email) { success, message ->
            if (!success) _errorMessage.postValue(message)
        }
    }
}
