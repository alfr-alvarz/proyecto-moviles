package com.example.tiendaguaumiau.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiendaguaumiau.data.AppDatabase
import com.example.tiendaguaumiau.data.network.RetrofitClient
import com.example.tiendaguaumiau.data.repository.UsuarioRepository

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val repository = UsuarioRepository(
                apiService = RetrofitClient.instance,
                db = AppDatabase.getInstance(context)
            )
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}