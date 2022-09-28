package com.example.clicktopost.ui.screens.auth.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clicktopost.domain.auth.models.AuthState
import com.example.clicktopost.ui.components.TextInput
import com.example.clicktopost.ui.screens.auth.AuthViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun SignUpForm(
    scaffoldState: ScaffoldState,
    setIsLoading: (Boolean) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val nameError = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }
    val confirmPasswordError = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val name by viewModel.signUpName.collectAsState()
    val email by viewModel.signUpEmail.collectAsState()
    val password by viewModel.signUpPassword.collectAsState()
    val confirmPassword by viewModel.signUpConfirmPassword.collectAsState()

    Column(modifier = Modifier.padding(horizontal = 15.dp)) {
        TextInput(
            value = name,
            label = "Name",
            error = nameError,
            onValueChange = { viewModel.signUpName.value = it }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextInput(
            value = email,
            label = "Email",
            error = emailError,
            keyboardType = KeyboardType.Email,
            onValueChange = { viewModel.signUpEmail.value = it }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextInput(
            value = password,
            label = "Password",
            error = passwordError,
            isPassword = true,
            keyboardType = KeyboardType.Password,
            onValueChange = { viewModel.signUpPassword.value = it }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextInput(
            value = confirmPassword,
            label = "Confirm password",
            error = confirmPasswordError,
            isPassword = true,
            keyboardType = KeyboardType.Password,
            onValueChange = { viewModel.signUpConfirmPassword.value = it }
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                focusManager.clearFocus()
                viewModel.signUp().onEach {
                    when (it) {
                        is AuthState.Loading -> {
                            setIsLoading(true)
                        }
                        is AuthState.Success -> {
                            setIsLoading(false)
                        }
                        is AuthState.Failure -> {
                            setIsLoading(false)
                            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                            scaffoldState.snackbarHostState.showSnackbar(it.message, "Okay")
                        }
                        is AuthState.InvalidEmail -> {
                            setIsLoading(false)
                            emailError.value = it.message
                        }
                        is AuthState.InvalidPassword -> {
                            setIsLoading(false)
                            passwordError.value = it.message
                        }
                        is AuthState.InvalidName -> {
                            setIsLoading(false)
                            nameError.value = it.message
                        }
                        is AuthState.InvalidConfirmPassword -> {
                            setIsLoading(false)
                            confirmPasswordError.value = it.message
                        }
                    }
                }.launchIn(scope)
            }
        ) {
            Text(text = "Sign Up")
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}