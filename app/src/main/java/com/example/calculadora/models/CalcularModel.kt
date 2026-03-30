package com.example.calculadora.models

data class CalcularModel(
    val displayText: String = "0",
    val firstNumber: Double? = null,
    val operation: String? = null,
    val shouldClear: Boolean = false
)