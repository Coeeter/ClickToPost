package com.example.clicktopost.domain.auth.useCases

import com.example.clicktopost.domain.auth.AuthRepository
import com.example.clicktopost.domain.auth.models.AuthState
import com.example.clicktopost.ui.utils.Validator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String): Flow<AuthState> {
        return flow {
            try {
                Validator.validateEmail(email)
            } catch (e: Exception) {
                emit(AuthState.InvalidEmail(e.message.toString()))
                return@flow
            }
            emit(AuthState.Loading)
            try {
                authRepository.sendPasswordResetLink(email)
                emit(AuthState.Success)
            } catch (e: Exception) {
                emit(AuthState.Failure(e.message.toString()))
            }
        }
    }
}