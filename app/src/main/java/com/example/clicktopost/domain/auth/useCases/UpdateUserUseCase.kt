package com.example.clicktopost.domain.auth.useCases

import com.example.clicktopost.domain.auth.AuthRepository
import com.example.clicktopost.domain.auth.models.AuthState
import com.example.clicktopost.domain.auth.models.UpdateUserOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    private val methods = hashMapOf(
        "username" to authRepository::updateUsername,
        "email" to authRepository::updateEmail,
        "message" to authRepository::updatePassword,
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
}