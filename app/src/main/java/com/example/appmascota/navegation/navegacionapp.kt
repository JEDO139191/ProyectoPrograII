package com.example.appmascota.navegation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmascota.Pantallas.HomeScreen
import com.example.appmascota.Pantallas.LoginScreen
import com.example.appmascota.Pantallas.ProfileScreen
import com.example.appmascota.Pantallas.RegisterScreen
import com.example.appmascota.Pantallas.SplashScreen
import com.example.appmascota.Pantallas.registerUser

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.route) {
        composable(AppScreens.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(AppScreens.LoginScreen.route) {
            LoginScreen(
                onLogin = { email, password ->
                    // Aquí manejarías el inicio de sesión
                },
                onNavigateToRegister = {
                    navController.navigate(AppScreens.RegisterScreen.route)
                }
            )
        }
        composable(AppScreens.RegisterScreen.route) {
            RegisterScreen(
                onRegister = { firstName, lastName, emailInput, password ->
                    registerUser(firstName, lastName, emailInput, password,
                        onSuccess = {
                            navController.navigate(AppScreens.ProfileScreen.route) // Navegar a ProfileScreen
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show() // Mostrar error
                        }
                    )
                },
                onNavigateToLogin = {
                    navController.navigate(AppScreens.LoginScreen.route) {
                        popUpTo(AppScreens.LoginScreen.route) { inclusive = true }
                    }
                }
            )
        }
        composable(AppScreens.ProfileScreen.route){
            ProfileScreen(
                onNavigateToHome = {
                    navController.navigate(AppScreens.HomeScreen.route){
                    }
                },
            )
        }
        composable(AppScreens.HomeScreen.route){
            HomeScreen()
        }
    }
}
