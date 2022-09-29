package com.example.clicktopost.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingComponent(visibility: Boolean) {
    if (visibility)
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.secondary.copy(alpha = if (MaterialTheme.colors.isLight) 0.5f else 0.2f),
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
}