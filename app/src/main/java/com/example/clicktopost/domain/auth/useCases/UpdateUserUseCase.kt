package com.example.clicktopost.domain.auth.useCases

import android.util.Log
import com.example.clicktopost.domain.auth.AuthRepository
import com.example.clicktopost.domain.auth.models.AuthState
import com.example.clicktopost.domain.auth.models.UpdateUserOptions
import com.example.clicktopost.ui.utils.Validator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    private val methods = hashMapOf(
        "username" to authRepository::updateUsername,
        "email" to authRepository::updateEmail,
        "password" to authRepository::updatePassword,
        "photo" to authRepository::updateProfilePhoto
    )

    operator fun <T> invoke(updateUserOptions: UpdateUserOptions<T>): Flow<AuthState> {
        return flow {
            emit(AuthState.Loading)
            try {
                methods[updateUserOptions.type]!!.call(updateUserOptions.argument)
                emit(AuthState.Success)
            } catch (e: Exception) {
                emit(AuthState.Failure(e.message.toString()))
            }
        }
    }

    fun updatePasswordFromDeepLink(
        oob: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<AuthState> {
        return flow {
            var isError = false
            try {
                Validator.validatePassword(newPassword, checkLength = true)
            } catch (e: Exception) {
                isError = true
                emit(AuthState.InvalidPassword(e.message.toString()))
            }
            try {
                Validator.validateConfirmPassword(newPassword, confirmPassword)
            } catch (e: Exception) {
                isError = true
                emit(AuthState.InvalidConfirmPassword(e.message.toString()))
            }
            if (isError) return@flow
            emit(AuthState.Loading)
            try {
                authRepository.updatePasswordFromDeepLink(oob, newPassword)
                emit(AuthState.Success)
            } catch (e: Exception) {
                Log.d("poly", e.message.toString())
                emit(AuthState.Failure(e.message.toString()))
            }
        }
    }
}