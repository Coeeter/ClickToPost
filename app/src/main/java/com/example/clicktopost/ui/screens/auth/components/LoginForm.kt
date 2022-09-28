package com.example.clicktopost.ui.screens.auth.components

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.clicktopost.R
import com.example.clicktopost.domain.auth.models.AuthState
import com.example.clicktopost.ui.components.TextInput
import com.example.clicktopost.ui.screens.auth.AuthViewModel
import com.example.clicktopost.ui.utils.Screens
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun LoginForm(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    setIsLoading: (Boolean) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val emailError = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val email by viewModel.loginEmail.collectAsState()
    val password by viewModel.loginPassword.collectAsState()

    suspend fun authStateListener(it: AuthState) {
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
            else -> {}
        }
    }

    val startForResult = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult
        val intent = it.data ?: return@rememberLauncherForActivityResult
        viewModel.loginUsingGoogle(intent)
            .onEach(::authStateListener)
            .launchIn(scope)
    }

    Column(
        modifier = Modifier.padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextInput(
            value = email,
            label = "Email",
            error = emailError,
            keyboardType = KeyboardType.Email,
            onValueChange = { viewModel.loginEmail.value = it }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextInput(
            value = password,
            label = "Password",
            error = passwordError,
            isPassword = true,
            keyboardType = KeyboardType.Password,
            onValueChange = { viewModel.loginPassword.value = it }
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                focusManager.clearFocus()
                viewModel.login().onEach(::authStateListener).launchIn(scope)
            }
        ) {
            Text(text = "Login")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            )
            Box(modifier = Modifier.background(MaterialTheme.colors.surface)) {
                Text(
                    text = "OR",
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(50.dp)
                        .background(
                            if (MaterialTheme.colors.isLight) MaterialTheme.colors.surface
                            else MaterialTheme.colors.onSurface.copy(alpha = 0.09f)
                        )
                )
            }
        }
        Button(
            onClick = {
                startForResult.launch(viewModel.getGoogleSignInIntent())
            },
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = "Google Logo",
                modifier = Modifier.width(30.dp)
            )
            Text(text = "Sign in with Google")
        }
        Spacer(modifier = Modifier.height(20.dp))
        TextButton(
            onClick = {
                navController.navigate(Screens.ForgetPasswordScreen.route)
            }
        ) {
            Text(text = "Forgot Password?")
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}