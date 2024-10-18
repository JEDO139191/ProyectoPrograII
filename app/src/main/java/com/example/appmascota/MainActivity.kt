package com.example.appmascota

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.appmascota.Pantallas.HomeScreen

class MainActivity : ComponentActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            MaterialTheme{
                Surface {
                    MyApp()
                }

            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MyApp() {
    // Aquí puedes configurar cualquier tema o estado que necesites
    HomeScreen() // Reemplaza con tu HomeScreen directamente
}

