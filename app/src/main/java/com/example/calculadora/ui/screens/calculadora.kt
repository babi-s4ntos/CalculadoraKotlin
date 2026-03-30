package com.example.calculadora.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.calculadora.models.CalcularModel

@Composable
fun CalculatorScreen() {

    var state by remember { mutableStateOf(CalcularModel()) }

    fun onNumberClick(number: String) {
        state = state.copy(
            displayText = if (state.displayText == "0" || state.shouldClear) {
                number
            } else {
                state.displayText + number
            },
            shouldClear = false
        )
    }

    fun onOperationClick(op: String) {
        state = state.copy(
            firstNumber = state.displayText.toDoubleOrNull(),
            operation = op,
            shouldClear = true
        )
    }

    fun onEqualsClick() {
        val secondNumber = state.displayText.toDoubleOrNull()

        if (state.firstNumber != null && secondNumber != null && state.operation != null) {

            val result = when (state.operation) {
                "+" -> state.firstNumber!! + secondNumber
                "-" -> state.firstNumber!! - secondNumber
                "*" -> state.firstNumber!! * secondNumber
                "/" -> {
                    if (secondNumber == 0.0) {
                        state = state.copy(displayText = "Erro")
                        return
                    }
                    state.firstNumber!! / secondNumber
                }
                else -> 0.0
            }

            fun formatResult(value: Double): String {
                return if (value % 1 == 0.0) {
                    value.toInt().toString()
                } else {
                    value.toString()
                }
            }

            state = state.copy(
                displayText = formatResult(result),
                firstNumber = null,
                operation = null,
                shouldClear = true
            )
        }
    }

    fun onClear() {
        state = CalcularModel()
    }

    val buttons = listOf(
        listOf("7", "8", "9", "/"),
        listOf("4", "5", "6", "*"),
        listOf("1", "2", "3", "-"),
        listOf("C", "0", "=", "+")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = state.displayText,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )

        Spacer(modifier = Modifier.height(40.dp))

        Column {
            buttons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { label ->
                        CalculatorButton(
                            text = label,
                            isSelected = label == state.operation
                        ) {
                            when (label) {
                                in "0".."9" -> onNumberClick(label)
                                "+", "-", "*", "/" -> onOperationClick(label)
                                "=" -> onEqualsClick()
                                "C" -> onClear()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(6.dp)
            .size(70.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.secondary
        )
    ) {
        Text(text = text, fontSize = 20.sp)
    }
}