package com.udb.personalexpensesapp.views

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udb.personalexpensesapp.R
import com.udb.personalexpensesapp.controller.ExpenseController
import com.udb.personalexpensesapp.model.Expense
import java.util.Calendar
import java.util.Locale

/**
 * Vista encargada del registro de gastos.
 */
class AddExpenseActivity : AppCompatActivity() {

    private lateinit var etExpenseName: EditText
    private lateinit var etExpenseAmount: EditText
    private lateinit var etExpenseCategory: EditText
    private lateinit var etExpenseDate: EditText

    private lateinit var btnSaveExpense: Button
    private lateinit var btnBackDashboard: Button

    private val expenseController = ExpenseController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_expense)

        initializeViews()
        setupListeners()
    }

    /**
     * Inicializa los componentes de la interfaz.
     */
    private fun initializeViews() {

        etExpenseName = findViewById(R.id.etExpenseName)
        etExpenseAmount = findViewById(R.id.etExpenseAmount)
        etExpenseCategory = findViewById(R.id.etExpenseCategory)
        etExpenseDate = findViewById(R.id.etExpenseDate)

        btnSaveExpense = findViewById(R.id.btnSaveExpense)
        btnBackDashboard = findViewById(R.id.btnBackDashboard)

    }

    /**
     * Configura los eventos de los botones.
     */
    private fun setupListeners() {

        btnSaveExpense.setOnClickListener {

            saveExpense()

        }

        btnBackDashboard.setOnClickListener {

            finish()

        }

    }

    /**
     * Valida y guarda un gasto.
     */
    private fun saveExpense() {

        val name = etExpenseName.text.toString().trim()
        val amountText = etExpenseAmount.text.toString().trim()
        val category = etExpenseCategory.text.toString().trim()
        val date = etExpenseDate.text.toString().trim()

        if (name.isEmpty()) {

            etExpenseName.error = "Ingrese un nombre"
            return

        }

        if (amountText.isEmpty()) {

            etExpenseAmount.error = "Ingrese un monto"
            return

        }

        val amount = amountText.toDoubleOrNull()

        if (amount == null || amount <= 0) {

            etExpenseAmount.error = "Monto inválido"
            return

        }

        if (category.isEmpty()) {

            etExpenseCategory.error = "Ingrese una categoría"
            return

        }

        if (date.isEmpty()) {

            etExpenseDate.error = "Ingrese una fecha"
            return

        }

        val calendar = Calendar.getInstance()

        val expense = Expense(
            name = name,
            amount = amount,
            category = category,
            date = date,
            month = calendar.get(Calendar.MONTH) + 1,
            year = calendar.get(Calendar.YEAR)
        )

        expenseController.addExpense(
            expense,

            onSuccess = {

                Toast.makeText(
                    this,
                    "Gasto registrado correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                clearFields()

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

    /**
     * Limpia los campos del formulario.
     */
    private fun clearFields() {

        etExpenseName.text.clear()
        etExpenseAmount.text.clear()
        etExpenseCategory.text.clear()
        etExpenseDate.text.clear()

    }

}