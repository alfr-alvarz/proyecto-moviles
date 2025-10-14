package com.example.tiendaguaumiau.ui.utils

// 1. Se cambia el import de LocalActivity por LocalContext
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import android.app.Activity


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {
    // 2. Obtenemos la Activity actual a través del LocalContext.
    // Esta es la forma moderna y recomendada.
    val activity = LocalContext.current as Activity
    return calculateWindowSizeClass(activity)
}