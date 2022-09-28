package com.example.clicktopost.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.clicktopost.ui.components.AppBar

@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = { AppBar(title = "Profile") }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Profile")
        }
    }
}