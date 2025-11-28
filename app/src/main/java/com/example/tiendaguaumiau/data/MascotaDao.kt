package com.example.tiendaguaumiau.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface MascotaDao {

    @Insert
    suspend fun insertarVarias(mascotas: List<Mascota>)

}