package com.example.tiendaguaumiau.ui.screens

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import com.example.tiendaguaumiau.ui.utils.obtenerWindowSizeClass

@Composable
fun HomeScreen2(){
    val windowSizeClass= obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass){
        windowSizeClass.Compact -> HomeScreenCompacta()
    }
}