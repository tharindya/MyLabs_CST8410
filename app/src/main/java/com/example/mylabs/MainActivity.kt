package com.example.mylabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylabs.ui.theme.MyLabsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyLabsTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.White
                    ) { innerPadding ->
                     MatchResultsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MatchResultsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "FC Bayern Match Results",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Row 1
        MatchRow(
            competition = "Bundesliga",
            homeTeam = "Elversberg",
            score = "1 – 2",
            awayTeam = "Bayern München"
        )
        // Row 2
        MatchRow(
            competition = "Bundesliga",
            homeTeam = "Bayern München",
            score = "7 – 0",
            awayTeam = "Union Berlin"
        )

        // Row 3
        MatchRow(
            competition = "Bundesliga",
            homeTeam = "Augsburg",
            score = "2 – 2",
            awayTeam = "Bayern München"
        )

        // Row 4
        MatchRow(
            competition = "Champions League",
            homeTeam = "Viking",
            score = "3 – 3",
            awayTeam = "Bayern München"
        )

        // Row 5
        MatchRow(
            competition = "Bundesliga",
            homeTeam = "Bayern München",
            score = "6 – 1",
            awayTeam = "RB Leipzig"
        )
    }
}

@Composable
fun MatchRow(
    competition: String,
    homeTeam: String,
    score: String,
    awayTeam: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Red ball on the LEFT
        Image(
            painter = painterResource(id = R.drawable.red_ball),
            contentDescription = "Match ball",
            modifier = Modifier
                .size(40.dp)
                .padding(end = 16.dp)
        )

        // Text in the MIDDLE
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = competition,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$homeTeam  $score  $awayTeam",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}