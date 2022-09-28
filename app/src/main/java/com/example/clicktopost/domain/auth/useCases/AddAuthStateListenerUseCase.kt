package com.example.clicktopost.domain.auth.useCases

import com.example.clicktopost.domain.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AddAuthStateListenerUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(listener: FirebaseAuth.AuthStateListener) {
        authRepository.addAuthStateListener(listener)
    }
}