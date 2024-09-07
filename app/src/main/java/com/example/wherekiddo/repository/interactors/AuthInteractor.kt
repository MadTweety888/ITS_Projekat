package com.example.wherekiddo.repository.interactors

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface AuthInteractor {

    val currentUser: FirebaseUser?

    fun hasUser(): Boolean

    fun getUserId(): String

    suspend fun signUpUser(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ): AuthResult

    suspend fun logInUser(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ): AuthResult

    fun logOutUser()
}