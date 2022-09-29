package com.example.clicktopost.ui.screens.auth

import android.content.Intent
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.clicktopost.domain.auth.models.AuthState
import com.example.clicktopost.domain.auth.models.DeepLinkData
import com.example.clicktopost.domain.auth.useCases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUserCase: RegisterUseCase,
    private val forgetPasswordUseCase: ResetPasswordUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getGoogleSignInClientUseCase: GetGoogleSignInClientUseCase,
) : ViewModel() {
    val loginEmail = MutableStateFlow("")
    val loginPassword = MutableStateFlow("")

    fun login(): Flow<AuthState> {
        return loginUseCase(loginEmail.value, loginPassword.value)
    }

    val signUpName = MutableStateFlow("")
    val signUpEmail = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")

    fun signUp(): Flow<AuthState> {
        return registerUserCase(
            signUpName.value,
            signUpEmail.value,
            password.value,
            confirmPassword.value
        )
    }

    val email = MutableStateFlow("")

    fun sendPasswordResetLinkToEmail(): Flow<AuthState> {
        return forgetPasswordUseCase(email.value)
    }

    fun resetPassword(oob: String) = updateUserUseCase.updatePasswordFromDeepLink(
        oob,
        password.value,
        confirmPassword.value
    )

    fun signOut() = signOutUseCase()

    fun getGoogleSignInIntent() = getGoogleSignInClientUseCase().signInIntent

    fun loginUsingGoogle(intent: Intent): Flow<AuthState> {
        return loginUseCase.loginWithGoogleCredentials(intent)
    }
}