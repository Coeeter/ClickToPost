package com.example.clicktopost.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.clicktopost.domain.auth.models.DeepLinkData
import com.example.clicktopost.ui.components.BottomNavigationBar
import com.example.clicktopost.ui.theme.ClickToPostTheme
import com.example.clicktopost.ui.utils.Screens
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            try {
                mainViewModel.getDeepLinkData(intent)
            } catch (e: Exception) {
                Log.d("poly", e.message.toString())
            }
        }
        setContent {
            ClickToPostTheme {
                val navController = rememberNavController()
                val startDestination by mainViewModel.startDestination.collectAsState()
                val isLoggedIn by mainViewModel.isLoggedIn.collectAsState()
                val deepLinkData by mainViewModel.deepLinkData.collectAsState()

                LaunchedEffect(deepLinkData) {
                    if (deepLinkData != null) {
                        val route = Screens.ResetPasswordScreen.route +
                                "/${deepLinkData?.mode}" +
                                "/${deepLinkData?.oobCode}"
                        navController.navigate(route)
                    }
                }

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