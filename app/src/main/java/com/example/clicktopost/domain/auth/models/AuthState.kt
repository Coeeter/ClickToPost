package com.example.clicktopost.domain.auth.models

sealed class AuthState {
    object Success : AuthState()
    object Loading : AuthState()
    class InvalidEmail(val message: String) : AuthState()
    class InvalidPassword(val message: String) : AuthState()
    class InvalidConfirmPassword(val message: String) : AuthState()
    class InvalidName(val message: String) : AuthState()
    class Failure(val message: String) : AuthState()
}