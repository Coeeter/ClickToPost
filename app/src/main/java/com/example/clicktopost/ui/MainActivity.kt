package com.example.clicktopost.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.clicktopost.ui.components.BottomNavigationBar
import com.example.clicktopost.ui.screens.auth.AuthViewModel
import com.example.clicktopost.ui.theme.ClickToPostTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClickToPostTheme {
                val navController = rememberNavController()
                val startDestination by authViewModel.startDestination.collectAsState()
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { if (isLoggedIn) BottomNavigationBar(navController = navController) }
                ) {
                    startDestination ?: return@Scaffold
                    NavGraph(
                        navController = navController,
                        startDestination = startDestination!!
                    )
                }
            }
        }
    }
}