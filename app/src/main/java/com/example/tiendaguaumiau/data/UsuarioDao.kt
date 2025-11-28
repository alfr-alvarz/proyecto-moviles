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

    @Transaction
    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    suspend fun getUsuarioConMascotasPorCorreo(correo: String): UsuarioConMascotas?

    @Query("DELETE FROM usuarios WHERE correo != 'ejemplo@duoc.cl'")
    suspend fun borrarTodosExceptoDemo()

    // FIX: Use the correct SQL column name 'id_usuario'
    @Query("DELETE FROM mascotas WHERE id_usuario NOT IN (SELECT id FROM usuarios WHERE correo = 'ejemplo@duoc.cl')")
    suspend fun borrarMascotasDeNoDemos()

    @Transaction
    suspend fun limpiarParaSincronizacion() {
        borrarMascotasDeNoDemos()
        borrarTodosExceptoDemo()
    }
}