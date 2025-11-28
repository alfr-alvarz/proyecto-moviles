package com.example.tiendaguaumiau.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVarios(usuarios: List<Usuario>)

    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena")
    suspend fun buscarPorCorreoYContrasena(correo: String, contrasena: String): Usuario?

    @Transaction
    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun getUsuarioConMascotas(id: Int): UsuarioConMascotas?

    @Transaction
    @Query("SELECT * FROM usuarios")
    fun getTodosUsuariosConMascotas(): Flow<List<UsuarioConMascotas>>
}