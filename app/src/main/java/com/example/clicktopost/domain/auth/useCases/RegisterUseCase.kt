package com.example.clicktopost.domain.auth.useCases

import com.example.clicktopost.domain.auth.AuthRepository
import com.example.clicktopost.domain.auth.models.AuthState
import com.example.clicktopost.ui.utils.Validator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Flow<AuthState> {
        return flow {
            var isError = false
            try {
                Validator.validateEmail(email)
            } catch (e: Exception) {
                isError = true
                emit(AuthState.InvalidEmail(e.message.toString()))
            }
            try {
                Validator.validatePassword(password, checkLength = true)
            } catch (e: Exception) {
                isError = true
                emit(AuthState.InvalidPassword(e.message.toString()))
            }
            try {
                Validator.validateConfirmPassword(password, confirmPassword)
            } catch (e: Exception) {
                isError = true
                emit(AuthState.InvalidConfirmPassword(e.message.toString()))
            }
            try {
                Validator.validateFieldIfEmpty("Name", name)
            } catch (e: Exception) {
                isError = true
                emit(AuthState.InvalidName(e.message.toString()))
            }
            if (isError) return@flow
            emit(AuthState.Loading)
            try {
                authRepository.register(name, email, password)
                emit(AuthState.Success)
            } catch (e: Exception) {
                emit(AuthState.Failure(e.message.toString()))
            }
        }
    }
}