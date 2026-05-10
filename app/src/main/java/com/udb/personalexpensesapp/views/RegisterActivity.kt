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
 * Vista encargada del registro de usuarios
 * mediante Firebase Authentication.
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var etRegisterEmail: EditText
    private lateinit var etRegisterPassword: EditText
    private lateinit var btnRegister: Button

    private val authController = AuthController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        initializeViews()
        setupListeners()
    }

    /**
     * Inicializa los componentes de la interfaz.
     */
    private fun initializeViews() {
        etRegisterEmail = findViewById(R.id.etRegisterEmail)
        etRegisterPassword = findViewById(R.id.etRegisterPassword)
        btnRegister = findViewById(R.id.btnRegister)
    }

    /**
     * Configura los eventos de los botones.
     */
    private fun setupListeners() {
        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    /**
     * Valida los datos y registra el usuario.
     */
    private fun registerUser() {
        val email = etRegisterEmail.text.toString().trim()
        val password = etRegisterPassword.text.toString().trim()

        if (email.isEmpty()) {
            etRegisterEmail.error = "Ingrese un correo"
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etRegisterEmail.error = "Correo inválido"
            return
        }

        if (password.isEmpty()) {
            etRegisterPassword.error = "Ingrese una contraseña"
            return
        }

        if (password.length < 6) {
            etRegisterPassword.error = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        authController.registerWithEmail(
            email,
            password,

            onSuccess = {
                Toast.makeText(
                    this,
                    "Usuario registrado correctamente",
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