package com.example.clicktopost.domain.auth.useCases

import android.content.Intent
import com.example.clicktopost.domain.auth.AuthRepository
import com.example.clicktopost.domain.auth.models.DeepLinkData
import javax.inject.Inject

class GetDataFromLink @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(intent: Intent): DeepLinkData? {
        return authRepository.getDataFromDeepLink(intent)
    }
}