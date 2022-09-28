package com.example.clicktopost.ui.utils

sealed class Screens(val route: String) {
    object AuthScreen : Screens("/auth")
    object ForgetPasswordScreen : Screens("/forget-message")
    object HomeScreen : Screens("/home")
    object ChatsScreen : Screens("/chats")
    object ExploreScreen : Screens("/explore")
    object SettingsScreen : Screens("/settings")
    object ProfileScreen : Screens("/profile")
}