package com.example.clicktopost.ui

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicktopost.domain.auth.models.DeepLinkData
import com.example.clicktopost.domain.auth.useCases.AddAuthStateListenerUseCase
import com.example.clicktopost.domain.auth.useCases.GetCurrentUserUseCase
import com.example.clicktopost.domain.auth.useCases.GetDataFromLink
import com.example.clicktopost.ui.utils.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getDeepLinkDataUserUseCase: GetDataFromLink,
    private val authStateListenerUseCase: AddAuthStateListenerUseCase,
) : ViewModel() {
    private val _deepLinkData = MutableStateFlow<DeepLinkData?>(null)
    val deepLinkData = _deepLinkData.asStateFlow()

    suspend fun getDeepLinkData(intent: Intent) {
        val data = getDeepLinkDataUserUseCase(intent)
        if (data?.mode.isNullOrEmpty() || data?.oobCode.isNullOrEmpty()) return
        _deepLinkData.value = data
    }

    private val _loggedInUser = MutableStateFlow(getCurrentUserUseCase())
    val loggedInUser = _loggedInUser.asStateFlow()

    val isLoggedIn = _loggedInUser.map { user -> user != null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val startDestination = isLoggedIn.map {
        if (it) Screens.HomeScreen.route
        else Screens.AuthScreen.route
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    init {
        getUserUpdates()
    }

    private fun getUserUpdates() {
        authStateListenerUseCase {
            _loggedInUser.value = it.currentUser
        }
    }
}