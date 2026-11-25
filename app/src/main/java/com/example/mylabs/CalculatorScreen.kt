package com.example.mylabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

/*
CURL command to test the server manually:

curl -X POST http://localhost:8080/calculate ^
  -H "Content-Type: application/json" ^
  -d "{\"num1\": 12, \"num2\": 4, \"operation\": \"multiply\"}"

Expected response: 48.0
*/

@Serializable
data class CalcRequest(
    val num1: Double,
    val num2: Double,
    val operation: String
)

@Composable
fun CalculatorScreen(modifier: Modifier = Modifier) {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("Result will appear here") }
    var isLoading by remember { mutableStateOf(false) }

    val client = remember {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    fun sendRequest(operation: String) {
        val n1 = num1.toDoubleOrNull()
        val n2 = num2.toDoubleOrNull()

        if (n1 == null || n2 == null) {
            result = "Please enter valid numbers"
            return
        }

        isLoading = true
        result = "Calculating..."

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Emulator → 10.0.2.2
                // Real phone → use your computer's IP (e.g. 192.168.1.25)
                val response = client.post("http://10.0.2.2:8080/calculate") {
                    contentType(ContentType.Application.Json)
                    setBody(CalcRequest(n1, n2, operation))
                }
                val body: String = response.body()
                result = "Result: $body"
            } catch (e: Exception) {
                result = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Calculator", fontSize = 28.sp)

        OutlinedTextField(
            value = num1,
            onValueChange = { num1 = it },
            label = { Text("First Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Second Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { sendRequest("add") },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) { Text("Add") }

            Button(
                onClick = { sendRequest("subtract") },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) { Text("Subtract") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { sendRequest("multiply") },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) { Text("Multiply") }

            Button(
                onClick = { sendRequest("divide") },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) { Text("Divide") }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = result,
            fontSize = 22.sp
        )
    }
}