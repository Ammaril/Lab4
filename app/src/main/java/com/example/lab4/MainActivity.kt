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
import kotlin.math.atan

/**
 * Лабораторная работа №2: Циклы
 * Вариант 2: Вычисление ряда 1/x - 1/(3x³) + 1/(5x) - 1/(7x⁷) + ...
 *
 * Ряд сходится при |x| > 1 и представляет собой разложение arctan(1/x)
 * Используется цикл while с неизвестным количеством итераций
 */
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

    // Состояния для полей ввода и результата
    var xText by remember { mutableStateOf("") }
    var epsText by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Вариант 2: 1/x − 1/(3x³) + 1/(5x⁵) − …",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Поле ввода x
        OutlinedTextField(
            value = xText,
            onValueChange = { xText = it },
            label = { Text("Введите x (|x| > 1)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Поле ввода точности epsilon
        OutlinedTextField(
            value = epsText,
            onValueChange = { epsText = it },
            label = { Text("Точность ε (например 0.0001)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )

        // Кнопка вычисления
        Button(
            onClick = {
                val x = xText.toDoubleOrNull()
                val epsilon = epsText.toDoubleOrNull()

                // Валидация ввода
                if (x == null || epsilon == null || epsilon <= 0) {
                    Toast.makeText(context, "Введите корректные числа!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (x == 0.0) {
                    Toast.makeText(context, "x не может быть равен 0!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // Проверка условия сходимости ряда
                if (abs(x) <= 1.0) {
                    Toast.makeText(
                        context,
                        "Ряд сходится только при |x| > 1!",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }

                // Вычисление ряда с использованием рекуррентной формулы:
                // a_0 = 1/x
                // a_{n+1} = a_n * (-1/x²) * (2n+1)/(2n+3)
                var term = 1.0 / x
                var sum = 0.0
                var count = 0
                var lastAddedTerm = 0.0
                var n = 0

                // Цикл продолжается, пока модуль слагаемого >= epsilon
                while (abs(term) >= epsilon) {
                    sum += term
                    lastAddedTerm = term
                    count++
                    term = term * (-1.0) / (x * x) * (2.0 * n + 1) / (2.0 * n + 3)
                    n++
                }

                // Контроль результата: сравнение с точным значением arctan(1/x)
                val exact = atan(1.0 / x)
                val error = abs(sum - exact)

                // Форматирование и вывод результатов
                resultText = """
                    Сумма ряда: ${"%.8f".format(sum)}
                    Точное значение arctan(1/x): ${"%.8f".format(exact)}
                    Погрешность: ${"%.8f".format(error)}
                    Последнее добавленное слагаемое: ${"%.8f".format(lastAddedTerm)}
                    Слагаемое, на котором остановились (< ε): ${"%.8f".format(term)}
                    Количество итераций цикла: $count
                """.trimIndent()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Вычислить")
        }

        // Вывод результата
        Text(
            text = resultText,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}