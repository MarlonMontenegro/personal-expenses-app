package com.udb.personalexpensesapp.controller

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.udb.personalexpensesapp.model.Expense
import java.util.Calendar

/**
 * Controlador encargado de gestionar
 * los gastos del usuario autenticado en Firestore.
 */
class ExpenseController {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    /**
     * Guarda un nuevo gasto en Firestore.
     */
    fun addExpense(
        expense: Expense,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            onError("No hay usuario autenticado")
            return
        }

        db.collection("users")
            .document(userId)
            .collection("expenses")
            .add(expense)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al guardar el gasto")
            }
    }

    /**
     * Obtiene el historial de gastos del usuario.
     */
    fun getExpenses(
        onSuccess: (List<Expense>) -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            onError("No hay usuario autenticado")
            return
        }

        db.collection("users")
            .document(userId)
            .collection("expenses")
            .get()
            .addOnSuccessListener { result ->
                val expenses = result.documents.mapNotNull { document ->
                    document.toObject(Expense::class.java)?.copy(
                        id = document.id
                    )
                }

                onSuccess(expenses)
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error al obtener los gastos")
            }
    }

    /**
     * Calcula el total de gastos del mes actual.
     */
    fun getMonthlyTotal(
        onSuccess: (Double) -> Unit,
        onError: (String) -> Unit
    ) {
        getExpenses(
            onSuccess = { expenses ->
                val calendar = Calendar.getInstance()
                val currentMonth = calendar.get(Calendar.MONTH) + 1
                val currentYear = calendar.get(Calendar.YEAR)

                val total = expenses
                    .filter { it.month == currentMonth && it.year == currentYear }
                    .sumOf { it.amount }

                onSuccess(total)
            },
            onError = { error ->
                onError(error)
            }
        )
    }
}