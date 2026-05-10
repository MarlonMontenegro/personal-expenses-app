package com.udb.personalexpensesapp.views

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udb.personalexpensesapp.R
import com.udb.personalexpensesapp.controller.AuthController

/**
 * Vista encargada del inicio de sesión
 * de usuarios mediante Firebase Authentication.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoRegister: Button

    private val authController = AuthController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        initializeViews()
        setupListeners()

    }

    /**
     * Inicializa los componentes de la interfaz.
     */
    private fun initializeViews() {

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoRegister = findViewById(R.id.btnGoRegister)

    }

    /**
     * Configura los eventos de los botones.
     */
    private fun setupListeners() {

        btnLogin.setOnClickListener {

            loginUser()

        }

        btnGoRegister.setOnClickListener {

            startActivity(
                Intent(this, RegisterActivity::class.java)
            )

        }

    }

    /**
     * Valida los datos e inicia sesión.
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

                Toast.makeText(
                    this,
                    "Inicio de sesión exitoso",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(this, DashboardActivity::class.java)
                )

                finish()

            },

            onError = { error ->

                Toast.makeText(
                    this,
                    error,
                    Toast.LENGTH_LONG
                ).show()

            }

        )

    }

}