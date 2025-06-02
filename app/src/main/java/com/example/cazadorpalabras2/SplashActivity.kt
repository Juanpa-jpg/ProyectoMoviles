package com.example.cazadorpalabras2

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.cazadordepalabras.R
import com.example.cazadorpalabras2.ui.theme.CazadorPalabrasTheme

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mostrar el composable con la imagen de splash
        setContent {
            CazadorPalabrasTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Reemplaza 'splash_image' con el nombre exacto de tu PNG en drawable
                    Image(
                        painter = painterResource(id = R.drawable.splash),
                        contentDescription = "Splash"
                    )
                }
            }
        }

        // Esperar 2 segundos y luego ir a MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // cerrar Splash para que con Back no regrese
        }, 2000) // 2000 ms = 2 segundos
    }
}
