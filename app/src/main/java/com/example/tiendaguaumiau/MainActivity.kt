package com.example.tiendaguaumiau

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tiendaguaumiau.ui.screens.HomeScreen2
import com.example.tiendaguaumiau.ui.screens.HomeScreenCompacta
import com.example.tiendaguaumiau.view.theme.TiendaGuauMiauTheme
import saludo

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            TiendaGuauMiauTheme {

                HomeScreenCompacta()

                /*
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    saludo()
                }
                */
            }
        }
    }
}

@Preview(name="Compact", widthDp = 360, heightDp = 800)
@Composable
fun PreviewCompact(){
    HomeScreenCompacta()
}