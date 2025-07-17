
package com.example.lesson4
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AzimuthCalculator()
                }
            }
        }
    }
}

@Composable
fun AzimuthCalculator() {
    // Состояния для координат
    var x1 by remember { mutableStateOf("0") }
    var y1 by remember { mutableStateOf("0") }
    var x2 by remember { mutableStateOf("0") }
    var y2 by remember { mutableStateOf("0") }

    // Состояния для результатов
    var azimuth by remember { mutableStateOf<Double?>(null) }
    var distance by remember { mutableStateOf<Double?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Поля ввода
        CoordinateInput("X корабля", x1) { x1 = it }
        Spacer(modifier = Modifier.height(8.dp))
        CoordinateInput("Y корабля", y1) { y1 = it }
        Spacer(modifier = Modifier.height(16.dp))
        CoordinateInput("X цели", x2) { x2 = it }
        Spacer(modifier = Modifier.height(8.dp))
        CoordinateInput("Y цели", y2) { y2 = it }
        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка расчета
        Button(onClick = {
            try {
                // Конвертация и расчет
                val x1d = x1.toDouble()
                val y1d = y1.toDouble()
                val x2d = x2.toDouble()
                val y2d = y2.toDouble()

                // Вычисление расстояния
                val dx = x2d - x1d
                val dy = y2d - y1d
                distance = sqrt(dx * dx + dy * dy)

                // Вычисление азимута
                azimuth = Math.toDegrees(atan2(dx, dy))
                errorMessage = null
            } catch (_: Exception) {
                errorMessage = "Ошибка ввода: используйте числа"
                azimuth = null
                distance = null
            }
        }) {
            Text("Рассчитать")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Отображение результатов
        when {
            errorMessage != null -> {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error
                )
            }
            azimuth != null && distance != null -> {
                Text("Азимут: ${"%.2f".format(azimuth)}°")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Расстояние: ${"%.2f".format(distance)}")
            }
            else -> {
                Text("Введите координаты и нажмите 'Рассчитать'")
            }
        }
    }
}

@Composable
fun CoordinateInput(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.width(250.dp)
    )
}

