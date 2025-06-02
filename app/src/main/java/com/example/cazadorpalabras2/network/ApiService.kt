package com.example.cazadorpalabras2.network

import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("/api/Palabras/GetPalabraAleatoriaFormada/")
    suspend fun getRandomWord(
        @Header("Token") token: String,
        @Header("Cliente") cliente: String
    ): WordResponse
}
