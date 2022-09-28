package com.example.clicktopost.ui.screens.auth

import androidx.compose.runtime.Composable

sealed class TabItem(val title: String, val composable: @Composable () -> Unit) {
    class LoginScreen(
        composable: @Composable () -> Unit
    ) : TabItem(
        title = "Login",
        composable = composable
    )

    class SignUpScreen(
        composable: @Composable () -> Unit
    ) : TabItem(
        title = "Sign up",
        composable = composable
    )
}