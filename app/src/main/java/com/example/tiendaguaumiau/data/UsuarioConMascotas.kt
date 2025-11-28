package com.example.tiendaguaumiau.data

import androidx.room.Embedded
import androidx.room.Relation

data class UsuarioConMascotas(
    @Embedded val usuario: Usuario,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_usuario"
    )
    val mascotas: List<Mascota>
)
