package com.udb.personalexpensesapp.views

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.udb.personalexpensesapp.R
import com.udb.personalexpensesapp.controller.AuthController

/**
 * Vista encargada del inicio de sesión
 * mediante correo y Google.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoogleLogin: Button
    private lateinit var btnGoRegister: Button

    private lateinit var googleSignInClient: GoogleSignInClient

    private val authController = AuthController()
    private val googleSignInRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        initializeViews()
        configureGoogleSignIn()
        setupListeners()
    }

    /**
     * Inicializa los componentes de la interfaz.
     */
    private fun initializeViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin)
        btnGoRegister = findViewById(R.id.btnGoRegister)
    }

    /**
     * Configura el cliente de inicio de sesión con Google.
     */
    private fun configureGoogleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(
            this,
            googleSignInOptions
        )
    }

    /**
     * Configura los eventos de los botones.
     */
    private fun setupListeners() {
        btnLogin.setOnClickListener {
            loginUser()
        }

        btnGoogleLogin.setOnClickListener {
            loginWithGoogle()
        }

        btnGoRegister.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }
    }

    /**
     * Valida los datos e inicia sesión con correo.
     */
    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty()) {
            etEmail.error = "Ingrese un correo"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Correo inválido"
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Ingrese una contraseña"
            return
        }

        authController.loginWithEmail(
            email,
            password,

            onSuccess = {
                goToDashboard("Inicio de sesión exitoso")
            },

            onError = { error ->
                showMessage(error)
            }
        )
    }

    /**
     * Abre el flujo de inicio de sesión con Google.
     */
    private fun loginWithGoogle() {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, googleSignInRequestCode)
    }

    /**
     * Recibe el resultado del inicio de sesión con Google.
     */
    @Deprecated("Método utilizado para compatibilidad con el flujo clásico de Google Sign-In.")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == googleSignInRequestCode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.result
                val idToken = account.idToken

                if (idToken == null) {
                    showMessage("No se pudo obtener el token de Google")
                    return
                }

                authController.loginWithGoogle(
                    idToken,

                    onSuccess = {
                        goToDashboard("Inicio de sesión con Google exitoso")
                    },

                    onError = { error ->
                        showMessage(error)
                    }
                )

            } catch (exception: Exception) {
                showMessage(exception.message ?: "Error al iniciar sesión con Google")
            }
        }
    }

    /**
     * Navega hacia la pantalla principal.
     */
    private fun goToDashboard(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()

        startActivity(
            Intent(this, DashboardActivity::class.java)
        )

        finish()
    }

    /**
     * Muestra un mensaje breve al usuario.
     */
    private fun showMessage(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}