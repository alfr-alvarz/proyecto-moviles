package com.example.tiendaguaumiau.ui.utils

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowssizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {
    return calculateWindowSizeClass(LocalActivity.current as android.app.Activity)
}