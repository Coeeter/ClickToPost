package com.example.clicktopost.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
fun ForgetPasswordScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }
    val emailError = remember { mutableStateOf("") }

    val email by viewModel.email.collectAsState()

    suspend fun authStateListener(it: AuthState) {
        when (it) {
            is AuthState.Loading -> {
                isLoading = true
            }
            is AuthState.Success -> {
                isLoading = false
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                scaffoldState.snackbarHostState.showSnackbar(
                    "Sent password reset link to email. Follow the instructions on the email to reset your password",
                    "Okay"
                )
                navController.popBackStack()
            }
            is AuthState.Failure -> {
                isLoading = false
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                scaffoldState.snackbarHostState.showSnackbar(it.message, "Okay")
            }
            is AuthState.InvalidEmail -> {
                isLoading = false
                emailError.value = it.message
            }
            else -> {}
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(title = "Forgot Password?") {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "")
                }
            }
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(15.dp))
            Surface(
                shape = CircleShape,
                border = BorderStroke(2.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.7f)),
                modifier = Modifier.size(100.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "",
                    modifier = Modifier.padding(20.dp),
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Enter your email registered with an account and the password reset link will be sent to your email",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 40.dp),
                color = MaterialTheme.colors.onSurface.copy(
                    alpha = 0.5f
                )
            )
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
                        value = email,
                        label = "Email",
                        error = emailError,
                        onValueChange = { viewModel.email.value = it },
                        keyboardType = KeyboardType.Email
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            focusManager.clearFocus(true)
                            viewModel.sendPasswordResetLinkToEmail()
                                .onEach(::authStateListener)
                                .launchIn(scope)
                        }
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
    LoadingComponent(visibility = isLoading)
}