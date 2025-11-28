package com.example.tiendaguaumiau.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO para los datos del usuario que vienen de la API.
 * Refleja la entidad Usuario de Spring Boot sin la lista de mascotas.
 */
@JsonClass(generateAdapter = true)
data class UsuarioDto(
    @Json(name = "id") val id: Long,
    @Json(name = "nombreCompleto") val nombreCompleto: String,
    @Json(name = "correo") val correo: String,
    @Json(name = "telefono") val telefono: String
)
