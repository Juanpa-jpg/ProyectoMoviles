package com.example.cazadorpalabras2.network

import com.google.gson.annotations.SerializedName

data class WordResponse(
    @SerializedName("entrada")
    val entrada: String,

    @SerializedName("superIndice")
    val superIndice: Int,

    @SerializedName("acepciones")
    val acepciones: List<String>
)
