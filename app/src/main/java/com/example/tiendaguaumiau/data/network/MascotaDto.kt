package com.example.tiendaguaumiau.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO para los datos de la mascota que vienen de la API.
 */
@JsonClass(generateAdapter = true)
data class MascotaDto(
    @Json(name = "id") val id: Long,
    @Json(name = "nombre") val nombre: String,
    @Json(name = "tipo") val tipo: String,
    @Json(name = "usuario") val usuario: UsuarioIdDto // Objeto anidado para obtener el ID
)

/**
 * DTO simple para extraer únicamente el ID del objeto de usuario anidado en MascotaDto.
 */
@JsonClass(generateAdapter = true)
data class UsuarioIdDto(
    @Json(name = "id") val id: Long
)
