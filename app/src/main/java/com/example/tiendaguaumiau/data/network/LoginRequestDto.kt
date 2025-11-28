package com.example.tiendaguaumiau.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequestDto(
    @Json(name = "correo") val correo: String,
    @Json(name = "contrasena") val contrasena: String
)
