package com.example.clicktopost.domain.auth.useCases

import com.example.clicktopost.domain.auth.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import javax.inject.Inject

class GetGoogleSignInClientUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    operator fun invoke(): GoogleSignInClient {
        return authRepo.getGoogleSignInClient()
    }
}