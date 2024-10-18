package com.example.appmascota.navegation

import android.health.connect.datatypes.ExerciseRoute

sealed class AppScreens(val route: String){
    object SplashScreen:AppScreens(route = "slashanimation")
    object LoginScreen:AppScreens(route = "login")
    object RegisterScreen:AppScreens(route = "register")
    object ProfileScreen:AppScreens(route = "perfil")
    object ProfileViewScreen:AppScreens(route = "vista")
    object EditProfileScreen :AppScreens(route = "edicion")
    object  HomeScreen : AppScreens(route = "home_screen")
}