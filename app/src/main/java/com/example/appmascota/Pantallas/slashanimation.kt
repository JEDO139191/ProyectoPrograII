package com.example.appmascota.Pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.appmascota.R
import com.example.appmascota.navegation.AppScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController){
    LaunchedEffect(Unit) {
        delay(3500)
        navController.popBackStack()
        navController.navigate(AppScreens.LoginScreen.route)
    }
    Splash()
}

@Composable
fun Splash(){
    Box(

    ){
        Image(
            painter = painterResource(id = R.drawable.huellas),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
    }
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(painter = painterResource(R.drawable.sin_fondo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(0.5f)
                .aspectRatio(1f)
                .offset(y = (-80).dp)
            )
    }
}
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    Splash()
}