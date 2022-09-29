package com.example.clicktopost.domain.auth.models

import android.net.Uri

sealed class UpdateUserOptions<T>(val type: String, val argument: T) {
    class Username(username: String) : UpdateUserOptions<String>("username", username)
    class Email(email: String) : UpdateUserOptions<String>("email", email)
    class Password(password: String) : UpdateUserOptions<String>("password", password)
    class ProfilePhoto(uri: Uri) : UpdateUserOptions<Uri>("photo", uri)
}