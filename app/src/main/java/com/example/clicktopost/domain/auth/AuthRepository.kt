package com.example.clicktopost.domain.auth

import android.net.Uri
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    fun getCurrentUser(): FirebaseUser?
    fun getGoogleSignInClient(): GoogleSignInClient

    suspend fun signInWithEmailAndPassword(email: String, password: String)
    suspend fun signInWithCredentials(credential: AuthCredential)
    suspend fun register(name: String, email: String, password: String)
    fun signOut()

    suspend fun updateEmail(email: String)
    suspend fun updatePassword(password: String)
    suspend fun updateUsername(name: String)
    suspend fun updateProfilePhoto(uri: Uri)
    suspend fun sendPasswordResetLink(email: String)

    fun addAuthStateListener(listener: FirebaseAuth.AuthStateListener)
}
