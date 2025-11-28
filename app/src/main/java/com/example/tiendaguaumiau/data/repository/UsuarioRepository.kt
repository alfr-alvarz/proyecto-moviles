package com.example.tiendaguaumiau.data.repository

import com.example.tiendaguaumiau.data.AppDatabase
import com.example.tiendaguaumiau.data.Mascota
import com.example.tiendaguaumiau.data.Usuario
import com.example.tiendaguaumiau.data.UsuarioConMascotas
import com.example.tiendaguaumiau.data.network.ApiService
import com.example.tiendaguaumiau.data.network.LoginRequestDto
import com.example.tiendaguaumiau.data.network.MascotaRegistroDto
import com.example.tiendaguaumiau.data.network.RegistroRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UsuarioRepository(private val apiService: ApiService, private val db: AppDatabase) {

    private val usuarioDao = db.usuarioDao()
    private val mascotaDao = db.mascotaDao()

    val usuariosConMascotas: Flow<List<UsuarioConMascotas>> = usuarioDao.getTodosUsuariosConMascotas()

    suspend fun actualizarDatosLocales(): Result<Unit> {
        return try {
            val usuariosDto = apiService.getUsuarios()
            val mascotasDto = apiService.getMascotas()

            val usuarios = usuariosDto.map { Usuario(id = it.id.toInt(), nombre = it.nombreCompleto, correo = it.correo, contrasena = "", telefono = it.telefono) }
            val mascotas = mascotasDto.map { Mascota(id = it.id.toInt(), nombre = it.nombre, tipo = it.tipo, idUsuario = it.usuario.id.toInt()) }

            usuarioDao.limpiarParaSincronizacion()
            
            usuarioDao.insertarVarios(usuarios)
            mascotaDao.insertarVarias(mascotas)
            
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(correo: String, contrasena: String): Result<UsuarioConMascotas> {
        return try {
            val response = apiService.login(LoginRequestDto(correo, contrasena))
            if (response.isSuccessful && response.body() != null) {
                actualizarDatosLocales().getOrThrow()
                
                val userId = response.body()!!.id.toInt()
                val usuarioCompleto = usuarioDao.getUsuarioConMascotas(userId)
                
                if (usuarioCompleto != null) {
                    Result.success(usuarioCompleto)
                } else {
                    Result.failure(Exception("Usuario no encontrado en la base de datos local tras el login."))
                }
            } else {
                Result.failure(Exception("Correo o contraseña incorrectos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // FIX: Lógica "Crear al Pulsar" para el modo demo.
    suspend fun loginComoInvitado(): Result<UsuarioConMascotas> {
        return try {
            var usuarioEjemplo = usuarioDao.getUsuarioConMascotasPorCorreo("ejemplo@duoc.cl")
            
            if (usuarioEjemplo == null) {
                // Si no existe, lo crea.
                val nuevoUsuario = Usuario(
                    nombre = "Usuario de Ejemplo",
                    correo = "ejemplo@duoc.cl",
                    contrasena = "", // No se necesita para demo
                    telefono = "12345678"
                )
                val idUsuario = usuarioDao.insertar(nuevoUsuario)

                val nuevaMascota = Mascota(
                    nombre = "Fido",
                    tipo = "Perro",
                    idUsuario = idUsuario.toInt()
                )
                mascotaDao.insertarVarias(listOf(nuevaMascota))
                
                // Lo busca de nuevo para asegurarse de que está completo.
                usuarioEjemplo = usuarioDao.getUsuarioConMascotasPorCorreo("ejemplo@duoc.cl")!!
            }
            
            Result.success(usuarioEjemplo)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registrar(nombre: String, correo: String, contrasena: String, telefono: String, mascotas: List<MascotaRegistroDto>): Result<Unit> {
        return try {
            val request = RegistroRequestDto(nombre, correo, contrasena, telefono, mascotas)
            val response = apiService.registrar(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception(errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}