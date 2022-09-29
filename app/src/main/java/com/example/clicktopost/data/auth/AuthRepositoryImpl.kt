package com.example.clicktopost.data.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.clicktopost.BuildConfig
import com.example.clicktopost.domain.auth.AuthRepository
import com.example.clicktopost.domain.auth.models.DeepLinkData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDynamicLinks: FirebaseDynamicLinks
) : AuthRepository {
    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun getDataFromDeepLink(intent: Intent): DeepLinkData? {
        val link = firebaseDynamicLinks
            .getDynamicLink(intent)
            .await()
            ?.link
            ?: return null
        val mode = link.getQueryParameter("mode") ?: return null
        val oob = link.getQueryParameter("oobCode") ?: return null
        firebaseAuth.verifyPasswordResetCode(oob).await()
        return DeepLinkData(mode, oob)
    }

    override fun addAuthStateListener(listener: FirebaseAuth.AuthStateListener) {
        firebaseAuth.addAuthStateListener(listener)
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signInWithCredentials(credential: AuthCredential) {
        firebaseAuth.signInWithCredential(credential).await()
    }

    override suspend fun sendPasswordResetLink(email: String) {
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setAndroidPackageName(
                "com.example.clicktopost",
                true,
                null
            )
            .setHandleCodeInApp(true)
            .setUrl("https://clicktopostapp.page.link/dmCn")
            .build()
        firebaseAuth.sendPasswordResetEmail(email, actionCodeSettings).await()
    }

    override fun getGoogleSignInClient(): GoogleSignInClient {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestIdToken(BuildConfig.SERVER_CLIENT_ID)
            .requestEmail()
            .requestProfile()
            .build()
        return GoogleSignIn.getClient(context, options)
    }

    override suspend fun register(name: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        updateUsername(name)
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun updateEmail(email: String) {
        firebaseAuth.currentUser!!.updateEmail(email).await()
    }

    override suspend fun updatePassword(password: String) {
        firebaseAuth.currentUser!!.updatePassword(password).await()
    }

    override suspend fun updatePasswordFromDeepLink(code: String, password: String) {
        val email = firebaseAuth.verifyPasswordResetCode(code).await()
        firebaseAuth.confirmPasswordReset(code, password).await()
        signInWithEmailAndPassword(email, password)
    }

    override suspend fun updateProfilePhoto(uri: Uri) {
        firebaseAuth.currentUser!!.updateProfile(
            UserProfileChangeRequest.Builder().setPhotoUri(uri).build()
        ).await()
    }

    override suspend fun updateUsername(name: String) {
        firebaseAuth.currentUser!!.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(name).build()
        ).await()
    }
}