package com.example.tiendaguaumiau.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para crear una instancia de DataStore a nivel de aplicación.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesRepository(private val context: Context) {

    // Clave para guardar la URI del fondo de pantalla como un String.
    private val backgroundImageKey = stringPreferencesKey("background_image_uri")

    /**
     * Flow que emite la URI del fondo de pantalla guardada cada vez que cambia.
     */
    val backgroundImageUri: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[backgroundImageKey]
        }

    /**
     * Guarda la URI de la imagen de fondo.
     * @param uriString La URI de la imagen convertida a String.
     */
    suspend fun saveBackgroundImageUri(uriString: String) {
        context.dataStore.edit {
            it[backgroundImageKey] = uriString
        }
    }
}