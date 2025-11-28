package com.example.tiendaguaumiau.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena")
    suspend fun buscarPorCorreoYContrasena(correo: String, contrasena: String): Usuario?

    @Transaction
    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun getUsuarioConMascotas(id: Int): UsuarioConMascotas?
}