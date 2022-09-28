package com.example.clicktopost.domain.auth.useCases

import com.example.clicktopost.domain.auth.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() {
        authRepository.signOut()
    }
}