package com.example.tiendaguaumiau.navigation

sealed class Screen(val route: String) {

    data object Home : Screen("home_page")

    data object Profile : Screen("profile_page")

    data object Settings : Screen("settings_page")

    data object  Login : Screen("login_page")

    data object Register : Screen ("register_page")

    data object Logued : Screen ("logued_page")

    //Ejemplo de una ruta a pantalla de detalles que requiere itemId

    data class Detail(val itemId:String) : Screen("detail_page/{itemId}"){
        //funcion para construir la ruta final con el argumento

        fun buildRoute(): String {
            //reemplazar placeholder por valor real
            return route.replace("{itemId}",itemId)
        }

        //si tenemos más argumentos, los agregamos a la data class y a la cadena de ruta
    }

}