package com.example.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.FirebaseApp
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

    private val auth: FirebaseAuth? by lazy {
        try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    val credentialManager: CredentialManager? by lazy {
        try {
            CredentialManager.create(context)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        try {
            val firebaseAuth = auth
            if (firebaseAuth != null) {
                val user = firebaseAuth.currentUser
                _currentUser.value = user
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                }
                firebaseAuth.addAuthStateListener { fa ->
                    val u = fa.currentUser
                    _currentUser.value = u
                    if (u != null) {
                        _authState.value = AuthState.Authenticated(u)
                    } else {
                        _authState.value = AuthState.Idle
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun signInWithEmail(email: String, pass: String): Result<FirebaseUser> {
        val firebaseAuth = auth ?: return Result.failure(Exception("Firebase Auth is not available on this device"))
        _authState.value = AuthState.Loading
        return suspendCoroutine { continuation ->
            try {
                firebaseAuth.signInWithEmailAndPassword(email, pass)
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
            } catch (e: Exception) {
                val msg = e.localizedMessage ?: "Sign-in exception"
                _authState.value = AuthState.Error(msg)
                continuation.resume(Result.failure(e))
            }
        }
    }

    suspend fun signUpWithEmail(email: String, pass: String): Result<FirebaseUser> {
        val firebaseAuth = auth ?: return Result.failure(Exception("Firebase Auth is not available on this device"))
        _authState.value = AuthState.Loading
        return suspendCoroutine { continuation ->
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
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
            } catch (e: Exception) {
                val msg = e.localizedMessage ?: "Sign-up exception"
                _authState.value = AuthState.Error(msg)
                continuation.resume(Result.failure(e))
            }
        }
    }

    suspend fun signInAnonymously(): Result<FirebaseUser> {
        val firebaseAuth = auth ?: return Result.failure(Exception("Firebase Auth is not available on this device"))
        _authState.value = AuthState.Loading
        return suspendCoroutine { continuation ->
            try {
                firebaseAuth.signInAnonymously()
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
            } catch (e: Exception) {
                val msg = e.localizedMessage ?: "Anonymous auth exception"
                _authState.value = AuthState.Error(msg)
                continuation.resume(Result.failure(e))
            }
        }
    }

    fun signOut() {
        try {
            auth?.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _authState.value = AuthState.Idle
    }

    fun resetAuthState() {
        try {
            val current = auth?.currentUser
            if (current != null) {
                _authState.value = AuthState.Authenticated(current)
            } else {
                _authState.value = AuthState.Idle
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Idle
        }
    }
}
