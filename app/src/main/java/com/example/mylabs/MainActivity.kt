package com.example.mylabs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mylabs.ui.theme.MyLabsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyLabsTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("assignment_info") {
            AssignmentInfoScreen(navController = navController)
        }
        composable("student_info") {
            StudentInfoScreen(navController = navController)
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Lab 4 – Intents & Navigation",
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 1. Phone Dialer
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:")
                }
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Phone Dialer")
        }

        // 2. Send Email
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_SUBJECT, "Lab 4 Information")
                }
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send Email")
        }

        // 3. Map Directions – Algonquin College
        Button(
            onClick = {
                val gmmIntentUri = Uri.parse("geo:0,0?q=Algonquin+College+Ottawa")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Map Directions")
        }

        // 4. Assignment Info (Navigation)
        Button(
            onClick = { navController.navigate("assignment_info") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Assignment Info")
        }

        // 5. Student Info (Navigation)
        Button(
            onClick = { navController.navigate("student_info") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Student Info")
        }
    }
}

@Composable
fun AssignmentInfoScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Course Code: CST 2335", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Course Title: Mobile Graphical Interface Programming", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Lab 4", fontSize = 22.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}

@Composable
fun StudentInfoScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Name: Tharindya Anjalika", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Email: anja0007@algonquinlive.com", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}