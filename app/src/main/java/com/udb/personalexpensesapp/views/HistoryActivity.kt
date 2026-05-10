package com.udb.personalexpensesapp.views

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udb.personalexpensesapp.R
import com.udb.personalexpensesapp.controller.ExpenseController

/**
 * Vista encargada de mostrar
 * el historial de gastos del usuario.
 */
class HistoryActivity : AppCompatActivity() {

    private lateinit var tvHistoryStatus: TextView
    private lateinit var lvExpenses: ListView
    private lateinit var btnBackFromHistory: Button

    private val expenseController = ExpenseController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_history)

        initializeViews()
        setupListeners()
        loadExpenses()
    }

    /**
     * Inicializa los componentes de la interfaz.
     */
    private fun initializeViews() {

        tvHistoryStatus = findViewById(R.id.tvHistoryStatus)
        lvExpenses = findViewById(R.id.lvExpenses)
        btnBackFromHistory = findViewById(R.id.btnBackFromHistory)

    }

    /**
     * Configura los eventos de los botones.
     */
    private fun setupListeners() {

        btnBackFromHistory.setOnClickListener {

            finish()

        }

    }

    /**
     * Obtiene y muestra el historial de gastos.
     */
    private fun loadExpenses() {

        expenseController.getExpenses(

            onSuccess = { expenses ->

                if (expenses.isEmpty()) {

                    tvHistoryStatus.text =
                        "No hay gastos registrados"

                    return@getExpenses

                }

                val expenseList = expenses.map { expense ->

                    "Nombre: ${expense.name}\n" +
                            "Monto: $${expense.amount}\n" +
                            "Categoría: ${expense.category}\n" +
                            "Fecha: ${expense.date}"

                }

                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    expenseList
                )

                lvExpenses.adapter = adapter

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