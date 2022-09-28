package com.example.clicktopost.di

import android.content.Context
import com.example.clicktopost.data.auth.AuthRepositoryImpl
import com.example.clicktopost.domain.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    fun providesFireStore() = FirebaseFirestore.getInstance()

    @Provides
    fun providesFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun providesAuthRepository(
        @ApplicationContext context: Context,
        firebaseAuth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(context, firebaseAuth)
}