package com.example.memoir.model

data class UserModel (
    var userId: String= "",
    var userName: String= "",
    var firstName: String="",
    var lastName: String="",
    var email: String="",
    var profileImageUrl: String?= null,
    var createdAt: Long = System.currentTimeMillis(),
    var lastLoginAt: Long = System.currentTimeMillis(),
    var theme: String = "light", // For theme preferences
    var notificationsEnabled: Boolean = true,
    var reminderTime: String = "20:00" // Default reminder time for journal entries
)

