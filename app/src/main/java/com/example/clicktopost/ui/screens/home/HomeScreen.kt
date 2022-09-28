package com.example.clicktopost.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.clicktopost.ui.components.AppBar
import com.example.clicktopost.ui.screens.auth.AuthViewModel

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { AppBar(title = "Home") }
    ) {
        val user by authViewModel.loggedInUser.collectAsState()
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(shape = CircleShape) {
                user?.photoUrl?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it.toString()),
                        contentDescription = null,
                        modifier = Modifier.size(128.dp)
                    )
                } ?: Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(128.dp)
                )
            }
            Text(text = user?.email.toString())
            Text(text = user?.displayName.toString())
            Text(text = user?.uid.toString())
            Button(onClick = { authViewModel.signOut() }) {
                Text(text = "Sign Out")
            }
        }
    }
}