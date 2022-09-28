package com.example.clicktopost.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null
) {
    Column(modifier = modifier) {
        TopAppBar(
            title = { Text(title) },
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            navigationIcon = navigationIcon
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
    }
}