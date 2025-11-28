package com.example.tiendaguaumiau.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegistroRequestDto(
    @Json(name = "nombreCompleto") val nombreCompleto: String,
    @Json(name = "correo") val correo: String,
    @Json(name = "contrasena") val contrasena: String,
    @Json(name = "telefono") val telefono: String,
    @Json(name = "mascotas") val mascotas: List<MascotaRegistroDto>
)

@JsonClass(generateAdapter = true)
data class MascotaRegistroDto(
    @Json(name = "nombre") val nombre: String,
    @Json(name = "tipo") val tipo: String
)
