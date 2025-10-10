package com.example.tiendaguaumiau.ui.utils.Window
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
enum class WindowType { Compact, Medium, Expanded }
data class WindowSizeClass(
    val widthType: WindowType,
    val heightType: WindowType
)
@Composable
fun rememberWindowSizeClass(): WindowSizeClass{
    val configuration= LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val widthType = when {
        screenWidth <600 -> WindowType.Compact
        screenHeight <840 -> WindowType.Medium
        else -> WindowType.Expanded
    }

    val heightType = when {
        screenHeight < 480 -> WindowType.Compact
        screenHeight < 900 -> WindowType.Medium
        else -> WindowType.Expanded
    }

    return WindowSizeClass(widthType,heightType)-
}