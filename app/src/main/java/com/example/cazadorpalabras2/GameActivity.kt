package com.example.cazadorpalabras2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cazadorpalabras2.network.WordResponse
import com.example.cazadorpalabras2.ui.theme.CazadorPalabrasTheme
import com.example.cazadorpalabras2.utils.WordSearchGenerator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException

class GameActivity : ComponentActivity() {

    private val boardWidth = 11
    private val boardHeight = 18

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val wordCount = intent.getIntExtra("WORD_COUNT", 5)

        setContent {
            CazadorPalabrasTheme {
                val context = LocalContext.current
                var boardState by remember { mutableStateOf<Array<CharArray>?>(null) }
                var placedWordsState by remember { mutableStateOf<List<String>>(emptyList()) }
                var selectedPositions by remember { mutableStateOf<List<Pair<Int, Int>>>(emptyList()) }
                var foundWords by remember { mutableStateOf(setOf<String>()) }
                var foundPositions by remember { mutableStateOf(setOf<Pair<Int, Int>>()) }

                // Cada vez que foundWords cambie, Compose lo reevaluará.
                // Cuando foundWords.size == placedWordsState.size, redirigimos a WinActivity.
                LaunchedEffect(Unit) {
                    val palabras = fetchMultiplePalabrasDesdeApi(wordCount)
                    if (palabras.isNotEmpty()) {
                        val (board, colocadas) = WordSearchGenerator.generate(boardWidth, boardHeight, palabras)
                        boardState = board
                        placedWordsState = colocadas
                    }
                }

                // Observador para redirigir cuando todas las palabras se encuentren
                LaunchedEffect(foundWords, placedWordsState) {
                    if (placedWordsState.isNotEmpty() && foundWords.size == placedWordsState.size) {
                        // Todas las palabras fueron encontradas: lanzamos WinActivity
                        context.startActivity(Intent(context, WinActivity::class.java))
                        if (context is ComponentActivity) context.finish()
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (boardState == null) {
                            BasicText(
                                text = "Cargando sopa de letras...",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val cellSize = 32.dp
                                val board = boardState!!
                                Column {
                                    board.forEachIndexed { rowIndex, row ->
                                        Row {
                                            row.forEachIndexed { colIndex, letter ->
                                                val pos = rowIndex to colIndex
                                                val isSelected = selectedPositions.contains(pos)
                                                val isFound = foundPositions.contains(pos)
                                                Box(
                                                    modifier = Modifier
                                                        .size(cellSize)
                                                        .padding(1.dp)
                                                        .background(
                                                            when {
                                                                isFound -> Color.Green
                                                                isSelected -> Color.Yellow
                                                                else -> Color.LightGray
                                                            }
                                                        )
                                                        .clickable {
                                                            if (!foundPositions.contains(pos)) {
                                                                selectedPositions = if (isSelected) {
                                                                    selectedPositions - pos
                                                                } else {
                                                                    selectedPositions + pos
                                                                }

                                                                val palabra = construirPalabra(board, selectedPositions)
                                                                val palabraReversa = palabra.reversed()

                                                                if (placedWordsState.contains(palabra) &&
                                                                    !foundWords.contains(palabra)
                                                                ) {
                                                                    // Marca palabra normal
                                                                    foundWords = foundWords + palabra
                                                                    foundPositions = foundPositions + selectedPositions
                                                                    selectedPositions = emptyList()
                                                                } else if (placedWordsState.contains(palabraReversa) &&
                                                                    !foundWords.contains(palabraReversa)
                                                                ) {
                                                                    // Marca palabra al revés
                                                                    foundWords = foundWords + palabraReversa
                                                                    foundPositions = foundPositions + selectedPositions
                                                                    selectedPositions = emptyList()
                                                                }
                                                            }
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    BasicText(
                                                        text = letter.toString(),
                                                        style = TextStyle(
                                                            fontSize = 18.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = Color.Black
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                BasicText(
                                    text = "Palabras a buscar:",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )

                                placedWordsState.forEach {
                                    val color = if (foundWords.contains(it)) Color.Green else Color.Black
                                    BasicText(
                                        text = "- $it",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            color = color
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(onClick = {
                                    startActivity(Intent(this@GameActivity, MainActivity::class.java))
                                    finish()
                                }) {
                                    Text("Inicio")
                                }

                                Button(onClick = {
                                    finishAffinity()
                                }) {
                                    Text("Salir")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun fetchMultiplePalabrasDesdeApi(cantidad: Int): List<String> {
        val palabras = mutableSetOf<String>()
        val client = OkHttpClient()

        withContext(Dispatchers.IO) {
            while (palabras.size < cantidad) {
                val request = Request.Builder()
                    .url("https://demapi.colmex.mx/api/Palabras/GetPalabraAleatoriaFormada/")
                    .addHeader("Token", "j1FUX2y7DklLgclZs5Nm6WaUjGcAgoR1")
                    .addHeader("cliente", "appsunam")
                    .get()
                    .build()

                try {
                    client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) return@use
                        val body = response.body?.string() ?: return@use
                        val gson = Gson()
                        val listType = object : TypeToken<List<WordResponse>>() {}.type
                        val palabraRespuesta: List<WordResponse> = gson.fromJson(body, listType)
                        palabraRespuesta.firstOrNull()?.entrada?.uppercase()?.let { palabra ->
                            val limpia = palabra.filter { it in 'A'..'Z' }
                            if (limpia.isNotEmpty()) {
                                palabras.add(limpia)
                            }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    break
                }
            }
        }

        return palabras.toList()
    }

    private fun construirPalabra(
        board: Array<CharArray>,
        posiciones: List<Pair<Int, Int>>
    ): String {
        return posiciones.joinToString("") { (fila, col) ->
            board.getOrNull(fila)?.getOrNull(col)?.toString() ?: ""
        }
    }
}
