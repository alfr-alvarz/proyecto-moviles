package com.example.tiendaguaumiau.ui.screens


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendaguaumiau.navigation.Screen
import com.example.tiendaguaumiau.viewmodel.LoginEvent
import com.example.tiendaguaumiau.viewmodel.LoginViewModel
import com.example.tiendaguaumiau.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: MainViewModel,
    vmLogin: LoginViewModel = viewModel()
) {

    val state by vmLogin.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        vmLogin.loginEvent.collectLatest { event ->
            when (event) {
                is LoginEvent.Success -> {
                    scope.launch {
                        // Guardamos el usuario en el MainViewModel
                        viewModel.setLoggedInUser(event.user)

                        // Mostramos el Snackbar
                        snackbarHostState.showSnackbar("¡Bienvenido, ${event.user.nombre}!")

                        // MODIFICADO: Navegamos a Home y limpiamos el historial
                        viewModel.navigateTo(
                            screen = Screen.Home,
                            popupToRoute = Screen.Login, // Eliminamos hasta Login...
                            inclusive = true,          // ...incluyendo Login.
                            singleTop = true           // Home es 'singleTop'
                        )
                    }
                }
                is LoginEvent.Error -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "GUAU&MIAU",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                    value = state.email,
                    onValueChange = vmLogin::onEmailChange,
                    label = { Text("CORREO") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !state.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.password,
                    onValueChange = vmLogin::onPasswordChange,
                    label = { Text("CONTRASEÑA") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = !state.isLoading // Deshabilitar campo si está cargando
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        vmLogin.onLoginClick()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                ) {
                    Text("Iniciar Sesión")
                }

                TextButton(
                    onClick = {
                        viewModel.navigateTo(Screen.Register)
                    },
                    enabled = !state.isLoading
                ) {
                    Text("¿No tienes cuenta? Regístrate")
                }
            }

            //  Overlay de Carga
            AnimatedVisibility(
                visible = state.isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)) // Fondo semitransparente
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

        }
    }
}