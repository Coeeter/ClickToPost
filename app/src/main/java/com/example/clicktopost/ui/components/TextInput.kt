package com.example.clicktopost.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TextInput(
    value: String,
    label: String,
    error: MutableState<String>,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    val isError by remember {
        derivedStateOf { error.value.isNotEmpty() }
    }
    var showPassword by remember {
        mutableStateOf(false)
    }
    val visualTransformation =
        if (isPassword && !showPassword) PasswordVisualTransformation()
        else VisualTransformation.None
    Column {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            value = value,
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = isError,
            visualTransformation = visualTransformation,
            trailingIcon = {
                if (isPassword)
                    IconButton(onClick = { showPassword = !showPassword }) {
                        val icon =
                            if (showPassword) Icons.Default.VisibilityOff
                            else Icons.Default.Visibility
                        val contentDescription =
                            if (showPassword) "Hide loginPassword"
                            else "Show loginPassword"
                        Icon(imageVector = icon, contentDescription = contentDescription)
                    }
            },
            onValueChange = {
                onValueChange(it)
                error.value = ""
            },
        )
        if (isError)
            Text(
                text = error.value,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 16.dp)
            )
    }
}