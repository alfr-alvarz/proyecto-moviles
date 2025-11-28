package com.example.tiendaguaumiau.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interfaz de Retrofit que define todos los endpoints del API.
 */
interface ApiService {

    // Endpoints de Auth
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<UsuarioDto>

    @POST("api/auth/registro")
    suspend fun registrar(@Body request: RegistroRequestDto): Response<UsuarioDto>

    // Endpoints para sincronización de datos
    @GET("api/usuarios")
    suspend fun getUsuarios(): List<UsuarioDto>

    @GET("api/mascotas")
    suspend fun getMascotas(): List<MascotaDto>
}
