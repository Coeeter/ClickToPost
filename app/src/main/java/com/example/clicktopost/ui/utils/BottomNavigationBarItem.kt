package com.example.clicktopost.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavigationBarItem(val title: String, val icon: ImageVector, val route: String) {
    HomeScreen(
        "Home",
        Icons.Default.Home,
        Screens.HomeScreen.route
    ),
    ExploreScreen(
        "Explore",
        Icons.Default.Explore,
        Screens.ExploreScreen.route
    ),
    ProfileScreen(
        "Profile",
        Icons.Default.Person,
        "${Screens.ProfileScreen.route}/{id}"
    ),
}