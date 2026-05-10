package com.udb.personalexpensesapp.model

data class Expense (
    val id: String = "",
    val name: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val date: String = "",
    val month: Int = 0,
    val year: Int = 0
)