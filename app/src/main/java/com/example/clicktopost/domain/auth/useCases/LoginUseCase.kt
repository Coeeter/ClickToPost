package com.example.clicktopost.domain.auth.useCases

import android.content.Intent
import com.example.clicktopost.domain.auth.AuthRepository
import com.example.clicktopost.domain.auth.models.AuthState
import com.example.clicktopost.ui.utils.Validator
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<AuthState> {
        return flow {
            var isError = false
            try {
                Validator.validateEmail(email)
            } catch (e: Exception) {
                isError = true
                emit(AuthState.InvalidEmail(e.message.toString()))
            }
            try {
                Validator.validatePassword(password)
            } catch (e: Exception) {
                isError = true
                emit(AuthState.InvalidPassword(e.message.toString()))
            }
            if (isError) return@flow
            emit(AuthState.Loading)
            try {
                authRepository.signInWithEmailAndPassword(email, password)
                emit(AuthState.Success)
            } catch (e: Exception) {
                emit(AuthState.Failure(e.message.toString()))
            }
        }
    }

    fun loginWithGoogleCredentials(intent: Intent): Flow<AuthState> {
        return flow {
            emit(AuthState.Loading)
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(intent).await()
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                authRepository.signInWithCredentials(credential)
                emit(AuthState.Success)
            } catch (e: Exception) {
                emit(AuthState.Failure(e.message.toString()))
            }
        }
    }
}