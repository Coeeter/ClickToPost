package com.example.clicktopost.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.clicktopost.ui.screens.auth.AuthScreen
import com.example.clicktopost.ui.screens.auth.ForgetPasswordScreen
import com.example.clicktopost.ui.screens.auth.ResetPasswordScreen
import com.example.clicktopost.ui.screens.chats.ChatsScreen
import com.example.clicktopost.ui.screens.explore.ExploreScreen
import com.example.clicktopost.ui.screens.home.HomeScreen
import com.example.clicktopost.ui.screens.profile.ProfileScreen
import com.example.clicktopost.ui.screens.settings.SettingsScreen
import com.example.clicktopost.ui.utils.Screens

@Composable
fun NavGraph(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = Screens.AuthScreen.route) {
            AuthScreen(navController)
        }
        composable(
            route = "${Screens.ResetPasswordScreen.route}/{mode}/{oob}",
            arguments = listOf(
                navArgument(name = "mode") {
                    type = NavType.StringType
                },
                navArgument(name = "oob") {
                    type = NavType.StringType
                }
            )
        ) {
            val mode = it.arguments?.getString("mode") ?: ""
            val oob = it.arguments?.getString("oob") ?: ""
            ResetPasswordScreen(navController = navController, mode = mode, oob = oob)
        }
        composable(route = Screens.ForgetPasswordScreen.route) {
            ForgetPasswordScreen(navController)
        }
        composable(route = Screens.HomeScreen.route) {
            HomeScreen()
        }
        composable(route = Screens.ChatsScreen.route) {
            ChatsScreen()
        }
        composable(route = Screens.ExploreScreen.route) {
            ExploreScreen()
        }
        composable(route = Screens.SettingsScreen.route) {
            SettingsScreen()
        }
        composable(
            route = "${Screens.ProfileScreen.route}/{id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            ProfileScreen()
        }
    }
}