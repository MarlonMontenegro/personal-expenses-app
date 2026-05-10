package com.udb.personalexpensesapp.controller

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

/**
 * Controlador encargado de gestionar
 * la autenticación de usuarios con Firebase.
 */
class AuthController {

    private val auth = FirebaseAuth.getInstance()

    /**
     * Registra un usuario con correo y contraseña.
     */
    fun registerWithEmail(
        email: String,
        password: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                onSuccess(result.user)
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al registrar usuario")
            }
    }

    /**
     * Inicia sesión con correo y contraseña.
     */
    fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                onSuccess(result.user)
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al iniciar sesión")
            }
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    fun logout() {
        auth.signOut()
    }

    /**
     * Obtiene el usuario autenticado actualmente.
     */
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    /**
     * Verifica si existe una sesión activa.
     */
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    /**
     * Inicia sesión con una cuenta de Google.
     */
    fun loginWithGoogle(
        idToken: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                onSuccess(result.user)
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al iniciar sesión con Google")
            }
    }
}