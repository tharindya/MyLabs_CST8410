package com.example.mylabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mylabs.ui.theme.MyLabsTheme

class MainActivity : ComponentActivity() {

    var FirstName: String = "Tharindya"
    var LastName: String = "Anjalika"

    fun printName(modifier: (String) -> String): String{
        val base = "Your First name is: $FirstName and your last name is: $LastName"
        return modifier(base)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyLabsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Yellow
                    ) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.hello_message,name),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyLabsTheme {
        Greeting("Android")
    }
}