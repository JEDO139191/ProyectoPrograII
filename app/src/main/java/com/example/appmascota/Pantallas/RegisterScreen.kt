package com.example.appmascota.Pantallas

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appmascota.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun RegisterScreen(onRegister: (String, String, String, String) -> Unit, onNavigateToLogin: () -> Unit) {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.mascota),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), //para los bordes
            verticalArrangement = Arrangement.Center, // vertical
            horizontalAlignment = Alignment.CenterHorizontally // contenido horizontal
        ) {
            Text(text = "Registro", style = MaterialTheme.typography.headlineMedium)

            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nombre de usuario") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellido de usuario") }
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    onRegister(firstName, lastName, email, password)
                } else {
                    Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Registrar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { onNavigateToLogin() }) {
                Text("¿Ya tienes una cuenta? Inicia sesión")
            }
        }
    }
}
fun registerUser(
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@addOnCompleteListener
                Log.d("RegisterUser", "Usuario registrado: $firstName $lastName $email")
                val userProfile = UserProfile(PetName = "", PetAge = "", PetBreed = "", PetGender = "", firstName = firstName, lastName = lastName, email = email)

                Firebase.firestore.collection("users").document(userId).set(userProfile)
                    .addOnSuccessListener {
                        Log.d("RegisterUser", "Perfil guardado exitosamente para $userId")
                        onSuccess()
                    }
                    .addOnFailureListener { exception ->
                        Log.e("RegisterUser", "Error al guardar datos: ${exception.message}")
                        onError("Error al guardar datos: ${exception.message}")
                    }
            } else {
                onError("Error en el registro: ${task.exception?.message}")
            }
        }
}



