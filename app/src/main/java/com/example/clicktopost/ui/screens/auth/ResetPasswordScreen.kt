package com.example.clicktopost.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.clicktopost.domain.auth.models.AuthState
import com.example.clicktopost.ui.components.AppBar
import com.example.clicktopost.ui.components.LoadingComponent
import com.example.clicktopost.ui.components.TextInput
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun ResetPasswordScreen(
    navController: NavHostController,
    mode: String,
    oob: String,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    var isLoading by remember { mutableStateOf(false) }
    val passwordError = remember { mutableStateOf("") }
    val confirmPasswordError = remember { mutableStateOf("") }

    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()

    LaunchedEffect(mode, oob) {
        if (mode != "resetPassword" || oob.isEmpty()) {
            scaffoldState.snackbarHostState.showSnackbar("Invalid link given")
            navController.popBackStack()
        }
    }

    suspend fun authStateListener(it: AuthState) {
        when (it) {
            is AuthState.Loading -> {
                isLoading = true
            }
            is AuthState.Success -> {
                isLoading = false
            }
            is AuthState.Failure -> {
                isLoading = false
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                scaffoldState.snackbarHostState.showSnackbar(it.message, "Okay")
            }
            is AuthState.InvalidPassword -> {
                isLoading = false
                passwordError.value = it.message
            }
            is AuthState.InvalidConfirmPassword -> {
                isLoading = false
                confirmPasswordError.value = it.message
            }
            else -> {}
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(title = "Reset password") {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "")
                }
            }
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(15.dp))
            Surface(
                modifier = Modifier.padding(horizontal = 30.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 15.dp,
                        end = 15.dp,
                        top = 5.dp,
                        bottom = 15.dp
                    )
                ) {
                    TextInput(
                        value = password,
                        label = "New password",
                        error = passwordError,
                        onValueChange = { viewModel.password.value = it },
                        isPassword = true,
                        keyboardType = KeyboardType.Password
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextInput(
                        value = confirmPassword,
                        label = "Confirm password",
                        error = confirmPasswordError,
                        onValueChange = { viewModel.confirmPassword.value = it },
                        isPassword = true,
                        keyboardType = KeyboardType.Password
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            focusManager.clearFocus(true)
                            viewModel.resetPassword(oob)
                                .onEach(::authStateListener)
                                .launchIn(scope)
                        }
                    ) {
                        Text("Reset Password")
                    }
                }
            }
        }
    }
    LoadingComponent(visibility = isLoading)
}