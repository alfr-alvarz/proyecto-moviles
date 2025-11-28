package com.example.tiendaguaumiau.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Usuario::class, Mascota::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun mascotaDao(): MascotaDao

    // La función clearAllTables() es heredada de RoomDatabase, no es necesario re-declararla.

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tienda_guau_miau_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Pre-popular la base de datos en el momento de la creación.
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                val usuarioDao = database.usuarioDao()
                                val mascotaDao = database.mascotaDao()

                                val usuarioEjemplo = Usuario(
                                    nombre = "Usuario de Ejemplo",
                                    correo = "ejemplo@duoc.cl",
                                    contrasena = "Pass.1234", // La contraseña no se usa para login local
                                    telefono = "12345678"
                                )
                                val idUsuario = usuarioDao.insertar(usuarioEjemplo)

                                val mascotaEjemplo = Mascota(
                                    nombre = "Fido",
                                    tipo = "Perro",
                                    idUsuario = idUsuario.toInt()
                                )
                                mascotaDao.insertarVarias(listOf(mascotaEjemplo))
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}