package com.example.androidapp.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class UserRole {
    DONOR,
    VOLUNTEER,
    HOSPITAL,
    ADMIN,
}

class AuthViewModel : ViewModel() {
    var isLoggedIn by mutableStateOf(false)
        private set

    var email by mutableStateOf("")
        private set

    var role by mutableStateOf<UserRole?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Very simple in-memory "account"
    private var registeredEmail: String? = null
    private var registeredPassword: String? = null
    private var registeredRole: UserRole? = null

    fun login(email: String, password: String) {
        errorMessage = null
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedEmail.isEmpty() || trimmedPassword.isEmpty()) {
            errorMessage = "Email and password are required."
            return
        }

        if (trimmedEmail == registeredEmail && trimmedPassword == registeredPassword) {
            isLoggedIn = true
            this.email = trimmedEmail
            this.role = registeredRole
        } else {
            errorMessage = "Invalid credentials. Please try again."
        }
    }

    fun register(email: String, password: String, confirm: String, role: UserRole) {
        errorMessage = null
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        val trimmedConfirm = confirm.trim()

        if (trimmedEmail.isEmpty() || trimmedPassword.isEmpty()) {
            errorMessage = "Email and password are required."
            return
        }
        if (trimmedPassword.length < 6) {
            errorMessage = "Password must be at least 6 characters."
            return
        }
        if (trimmedPassword != trimmedConfirm) {
            errorMessage = "Passwords do not match."
            return
        }

        registeredEmail = trimmedEmail
        registeredPassword = trimmedPassword
        registeredRole = role
        // Auto-log in after registration
        isLoggedIn = true
        this.email = trimmedEmail
        this.role = role
    }

    fun logout() {
        isLoggedIn = false
        email = ""
        role = null
    }
}

