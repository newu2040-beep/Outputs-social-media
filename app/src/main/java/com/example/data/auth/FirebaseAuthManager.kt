package com.example.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}

class FirebaseAuthManager(private val context: Context) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val credentialManager: CredentialManager = CredentialManager.create(context)

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(
        auth.currentUser?.let { AuthState.Authenticated(it) } ?: AuthState.Idle
    )
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _currentUser.value = user
            if (user != null) {
                _authState.value = AuthState.Authenticated(user)
            } else {
                _authState.value = AuthState.Idle
            }
        }
    }

    suspend fun signInWithEmail(email: String, pass: String): Result<FirebaseUser> {
        _authState.value = AuthState.Loading
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        _authState.value = AuthState.Authenticated(user)
                        continuation.resume(Result.success(user))
                    } else {
                        val err = "Authentication succeeded but user was null"
                        _authState.value = AuthState.Error(err)
                        continuation.resume(Result.failure(Exception(err)))
                    }
                }
                .addOnFailureListener { exception ->
                    val msg = exception.localizedMessage ?: "Email sign-in failed"
                    _authState.value = AuthState.Error(msg)
                    continuation.resume(Result.failure(exception))
                }
        }
    }

    suspend fun signUpWithEmail(email: String, pass: String): Result<FirebaseUser> {
        _authState.value = AuthState.Loading
        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        _authState.value = AuthState.Authenticated(user)
                        continuation.resume(Result.success(user))
                    } else {
                        val err = "Registration succeeded but user was null"
                        _authState.value = AuthState.Error(err)
                        continuation.resume(Result.failure(Exception(err)))
                    }
                }
                .addOnFailureListener { exception ->
                    val msg = exception.localizedMessage ?: "Email registration failed"
                    _authState.value = AuthState.Error(msg)
                    continuation.resume(Result.failure(exception))
                }
        }
    }

    suspend fun signInAnonymously(): Result<FirebaseUser> {
        _authState.value = AuthState.Loading
        return suspendCoroutine { continuation ->
            auth.signInAnonymously()
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        _authState.value = AuthState.Authenticated(user)
                        continuation.resume(Result.success(user))
                    } else {
                        val err = "Anonymous sign-in succeeded but user was null"
                        _authState.value = AuthState.Error(err)
                        continuation.resume(Result.failure(Exception(err)))
                    }
                }
                .addOnFailureListener { exception ->
                    val msg = exception.localizedMessage ?: "Anonymous sign-in failed"
                    _authState.value = AuthState.Error(msg)
                    continuation.resume(Result.failure(exception))
                }
        }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Idle
    }

    fun resetAuthState() {
        if (auth.currentUser != null) {
            _authState.value = AuthState.Authenticated(auth.currentUser!!)
        } else {
            _authState.value = AuthState.Idle
        }
    }
}
