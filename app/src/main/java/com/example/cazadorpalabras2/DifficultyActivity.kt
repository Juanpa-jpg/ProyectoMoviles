package com.example.cazadorpalabras2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cazadorpalabras2.ui.theme.CazadorPalabrasTheme

class DifficultyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CazadorPalabrasTheme {
                DifficultyScreen()
            }
        }
    }
}

@Composable
fun DifficultyScreen() {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Selecciona la dificultad",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(36.dp))

            // Solo enviamos WORD_COUNT, el tamaño del tablero será fijo en GameActivity

            Button(
                onClick = {
                    val intent = Intent(context, GameActivity::class.java).apply {
                        putExtra("WORD_COUNT", 3)
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp)
            ) {
                Text(text = "Fácil")
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val intent = Intent(context, GameActivity::class.java).apply {
                        putExtra("WORD_COUNT", 4)
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp)
            ) {
                Text(text = "Medio")
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val intent = Intent(context, GameActivity::class.java).apply {
                        putExtra("WORD_COUNT", 5)
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp)
            ) {
                Text(text = "Difícil")
            }
        }
    }
}
