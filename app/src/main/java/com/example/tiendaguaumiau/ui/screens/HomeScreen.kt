package com.example.tiendaguaumiau.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendaguaumiau.navigation.Screen
import com.example.tiendaguaumiau.viewmodel.MainViewModel
import com.example.tiendaguaumiau.viewmodel.PetData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Observamos el estado del usuario logueado desde el MainViewModel
    val currentUser by viewModel.currentUser.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                //  Mostrar nombre del usuario si existe
                currentUser?.let { user ->
                    Text(
                        text = "Hola, ${user.nombre}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                } ?: run {
                    Text("Menú", modifier = Modifier.padding(16.dp))
                }

                HorizontalDivider()

                NavigationDrawerItem(label = { Text(text = "Ir a Perfil") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Profile)
                    }
                )

                NavigationDrawerItem(label = { Text(text = "Configuración") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Settings)
                    }
                )

                //  Botón de Cerrar Sesión
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(label = { Text(text = "Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.logout() // Llama a la función de logout
                    },
                    icon = { Icon(Icons.Default.Close, contentDescription = "Cerrar Sesión") }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Mis Mascotas") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { innerPadding ->


            // Comprobar si el usuario está cargado
            if (currentUser == null) {
                // Muestra un indicador de carga si el usuario aún es nulo
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                    Text("Cargando usuario...")
                }
            } else {
                // El usuario está cargado, mostramos sus mascotas
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding) // Usamos el innerPadding
                        .padding(horizontal = 16.dp, vertical = 8.dp), // Padding extra
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Saludo
                    item {
                        Text(
                            "¡Hola, ${currentUser!!.nombre}!", // !! es seguro por el check
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Estas son tus mascotas registradas:",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    // Lista de Mascotas
                    if (currentUser!!.pets.isEmpty()) {
                        item {
                            Text("Aún no tienes mascotas registradas.")
                        }
                    } else {
                        items(currentUser!!.pets) { pet ->
                            // Usamos el PetListItem de la sugerencia anterior
                            PetListItem(pet = pet)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PetListItem(pet: PetData) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    pet.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    pet.type,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}