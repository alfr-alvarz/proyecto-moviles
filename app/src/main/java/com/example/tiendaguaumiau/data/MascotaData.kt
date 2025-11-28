package com.example.tiendaguaumiau.data

import android.net.Uri

/**
 * Clase de datos para la UI, ahora con un campo para la URI de la imagen.
 */
data class MascotaData(
    val nombre: String = "",
    val tipo: String = "",
    val imagenUri: Uri? = null
)
