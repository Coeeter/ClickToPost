package com.example.clicktopost.ui.screens.forgetPassword

import androidx.lifecycle.ViewModel
import com.example.clicktopost.domain.auth.models.AuthState
import com.example.clicktopost.domain.auth.useCases.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val forgetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    val email = MutableStateFlow("")

    fun resetPassword(): Flow<AuthState> {
        return forgetPasswordUseCase(email.value)
    }
}