package com.example.clicktopost.ui.screens.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicktopost.domain.auth.models.AuthState
import com.example.clicktopost.domain.auth.useCases.*
import com.example.clicktopost.ui.utils.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    getCurrentUserUseCase: GetCurrentUserUseCase,
    private val loginUseCase: LoginUseCase,
    private val registerUserCase: RegisterUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getGoogleSignInClientUseCase: GetGoogleSignInClientUseCase,
    private val authStateListenerUseCase: AddAuthStateListenerUseCase,
) : ViewModel() {
    val loginEmail = MutableStateFlow("")
    val loginPassword = MutableStateFlow("")

    fun login(): Flow<AuthState> {
        return loginUseCase(loginEmail.value, loginPassword.value)
    }

    val signUpName = MutableStateFlow("")
    val signUpEmail = MutableStateFlow("")
    val signUpPassword = MutableStateFlow("")
    val signUpConfirmPassword = MutableStateFlow("")

    fun signUp(): Flow<AuthState> {
        return registerUserCase(
            signUpName.value,
            signUpEmail.value,
            signUpPassword.value,
            signUpConfirmPassword.value
        )
    }

    fun signOut() = signOutUseCase()

    fun getGoogleSignInIntent() = getGoogleSignInClientUseCase().signInIntent

    fun loginUsingGoogle(intent: Intent): Flow<AuthState> {
        return loginUseCase.loginWithGoogleCredentials(intent)
    }

    private val _loggedInUser = MutableStateFlow(getCurrentUserUseCase())
    val loggedInUser = _loggedInUser.asStateFlow()

    val isLoggedIn = _loggedInUser.map { user -> user != null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val startDestination = isLoggedIn.map {
        if (it) return@map Screens.HomeScreen.route
        Screens.AuthScreen.route
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    init {
        getUserUpdates()
    }

    private fun getUserUpdates() {
        authStateListenerUseCase {
            _loggedInUser.value = it.currentUser
        }
    }
}