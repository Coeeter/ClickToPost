package com.example.clicktopost.ui.utils

import android.util.Patterns

object Validator {
    fun validateFieldIfEmpty(field: String, value: String) {
        if (value.isEmpty()) throw Exception("$field required!")
    }

    fun validateEmail(email: String) {
        validateFieldIfEmpty("Email", email)
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            throw Exception("Invalid Email address provided!")
    }

    fun validatePassword(password: String, checkLength: Boolean = false) {
        validateFieldIfEmpty("Password", password)
        if (!checkLength) return
        if (password.length < 6)
            throw Exception("Password should be at least 6 characters long!")
    }

    fun validateConfirmPassword(password: String, confirmPassword: String) {
        if (password != confirmPassword)
            throw Exception("Passwords do not match!")
    }
}