package com.udb.personalexpensesapp.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udb.personalexpensesapp.R
import com.udb.personalexpensesapp.controller.AuthController
import com.udb.personalexpensesapp.controller.ExpenseController

/**
 * Vista encargada de mostrar
 * el resumen mensual de gastos.
 */
class DashboardActivity : AppCompatActivity() {

    private lateinit var tvMonthlyTotal: TextView
    private lateinit var btnAddExpense: Button
    private lateinit var btnViewHistory: Button
    private lateinit var btnLogout: Button

    private val authController = AuthController()
    private val expenseController = ExpenseController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        initializeViews()
        setupListeners()
        loadMonthlyTotal()
    }

    /**
     * Recarga el total mensual al volver a la vista.
     */
    override fun onResume() {
        super.onResume()
        loadMonthlyTotal()
    }

    /**
     * Inicializa los componentes de la interfaz.
     */
    private fun initializeViews() {
        tvMonthlyTotal = findViewById(R.id.tvMonthlyTotal)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        btnViewHistory = findViewById(R.id.btnViewHistory)
        btnLogout = findViewById(R.id.btnLogout)
    }

    /**
     * Configura los eventos de los botones.
     */
    private fun setupListeners() {
        btnAddExpense.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        btnViewHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        btnLogout.setOnClickListener {
            authController.logout()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    /**
     * Obtiene y muestra el total de gastos del mes actual.
     */
    private fun loadMonthlyTotal() {
        expenseController.getMonthlyTotal(
            onSuccess = { total ->
                tvMonthlyTotal.text = "Total del mes: $${String.format("%.2f", total)}"
            },
            onError = { error ->
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        )
    }
}