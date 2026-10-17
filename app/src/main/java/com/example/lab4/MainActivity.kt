package com.example.lab4

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.lab4.ui.theme.Lab4Theme
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab4Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var xText by remember { mutableStateOf("") }
    var epsText by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Вариант 2: 1/x − 1/(3x³) + 1/(5x⁵) − …",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = xText,
            onValueChange = { xText = it },
            label = { Text("Введите x (|x| > 1)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = epsText,
            onValueChange = { epsText = it },
            label = { Text("Точность ε (например 0.0001)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                val x = xText.toDoubleOrNull()
                val epsilon = epsText.toDoubleOrNull()

                if (x == null || epsilon == null || epsilon <= 0) {
                    Toast.makeText(context, "Введите корректные числа!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (x == 0.0) {
                    Toast.makeText(context, "x не может быть равен 0!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (abs(x) <= 1.0) {
                    Toast.makeText(
                        context,
                        "Ряд сходится только при |x| > 1!",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }

                // Вычисление ряда: 1/x - 1/(3x³) + 1/(5x⁵) - 1/(7x⁷) + ...
                var term = 1.0 / x
                var sum = 0.0
                var count = 0
                var n = 0

                while (abs(term) >= epsilon) {
                    sum += term
                    count++
                    term = term * (-1.0) / (x * x) * (2.0 * n + 1) / (2.0 * n + 3)
                    n++
                }

                var lastAddedTerm = 0.0
                var termForDisplay = term

                // Пересчёт с сохранением последнего слагаемого
                var t = 1.0 / x
                sum = 0.0
                count = 0
                n = 0
                while (abs(t) >= epsilon) {
                    sum += t
                    lastAddedTerm = t
                    count++
                    t = t * (-1.0) / (x * x) * (2.0 * n + 1) / (2.0 * n + 3)
                    n++
                }
                termForDisplay = t

                resultText = """
                    Сумма ряда: ${"%.8f".format(sum)}
                    Последнее добавленное слагаемое: ${"%.8f".format(lastAddedTerm)}
                    Слагаемое, на котором остановились (< ε): ${"%.8f".format(termForDisplay)}
                    Количество итераций цикла: $count
                """.trimIndent()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Вычислить")
        }

        Text(
            text = resultText,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}