package com.example.appmascota.Pantallas

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.appmascota.R
import com.example.appmascota.navegation.AppScreens
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

@Composable
fun ProfileScreen(onNavigateToHome: () -> Unit)
{
    val currentUser = FirebaseAuth.getInstance().currentUser
    val email = currentUser?.email ?: ""

    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var PetName by rememberSaveable { mutableStateOf("") }
    var PetAge by rememberSaveable { mutableStateOf("") }
    var PetBreed by rememberSaveable { mutableStateOf("") }
    var PetGender by rememberSaveable { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    // Lógica para seleccionar la imagen
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }
    LaunchedEffect(currentUser) {
        loadProfileData { userProfile ->
            if (userProfile != null) {
                firstName = userProfile.firstName
                lastName = userProfile.lastName
                PetName = userProfile.PetName
                PetAge = userProfile.PetAge
                PetBreed = userProfile.PetBreed
                PetGender = userProfile.PetGender
            } else {
                Log.d("ProfileScreen", "UserProfile es nulo")
            }
        }
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.mascota),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenido encima del fondo
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen circular para la foto de perfil
            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Image(
                    painter = painterResource(id = R.drawable.ic_stat_name), // Imagen por defecto
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campos de texto para la información del perfil
            TextField(
                value = PetName,
                onValueChange = { PetName = it },
                label = { Text("Nombre de mascota") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = PetAge,
                onValueChange = { PetAge = it },
                label = { Text("Edad de mascota") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = PetBreed,
                onValueChange = { PetBreed = it },
                label = { Text("Raza de mascota") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = PetGender,
                onValueChange = { PetGender = it },
                label = { Text("Sexo de mascota") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para seleccionar la foto de perfil
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Seleccionar foto de perfil")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar
            Button(onClick = {
                saveProfileData(firstName, lastName, email, PetName, PetAge, PetBreed, PetGender) {
                    val uriToUpload = imageUri
                    if (uriToUpload != null) {
                        uploadImageToFirebase(uriToUpload) {
                            Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                            Log.d("ProfileScreen", "Navegando a HomeScreen")
                            onNavigateToHome()

                        }
                    } else {
                        Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                        Log.d("ProfileScreen", "Navegando a HomeScreen")
                        onNavigateToHome()
                    }
                }
            }) {
                Text("Guardar y continuar")
            }

        }
    }
}
fun saveProfileData(
    firstName: String,
    lastName: String,
    email: String,
    PetName: String,
    PetAge: String,
    PetBreed: String,
    PetGender: String,
    onComplete: () -> Unit
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "default_user_id"
    val userProfile = UserProfile(PetName, PetAge, PetBreed, PetGender, firstName, lastName, email)

    Firebase.firestore.collection("users").document(userId).set(userProfile)
        .addOnSuccessListener {
            Log.d("ProfileScreen", "Perfil guardado exitosamente")
            onComplete()
        }
        .addOnFailureListener { exception ->
            Log.e("Firebase", "Error al guardar los datos: ${exception.message}")
            onComplete()
        }
}

private fun uploadImageToFirebase(imageUri: Uri, onComplete: () -> Unit) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "default_user_id"
    val profilePicRef = storageRef.child("profile_pics/$userId.jpg")

    profilePicRef.putFile(imageUri)
        .addOnSuccessListener {
            Log.d("Firebase", "Imagen subida exitosamente")
            onComplete()
        }
        .addOnFailureListener { exception ->
            Log.e("Firebase", "Error al subir la imagen: ${exception.message}")
            onComplete()
        }
}

fun loadProfileData(onComplete: (UserProfile?) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    Firebase.firestore.collection("users").document(userId).get()
        .addOnSuccessListener { document ->
            if (document != null) {
                val userProfile = document.toObject(UserProfile::class.java)
                Log.d("LoadProfileData", "Datos recuperados: ${userProfile?.firstName} ${userProfile?.lastName} ${userProfile?.email}")
                onComplete(userProfile)
            } else {
                Log.d("LoadProfileData", "No se encontraron datos para el usuario")
                onComplete(null)
            }
        }
        .addOnFailureListener { exception ->
            Log.e("LoadProfileData", "Error al cargar los datos: ${exception.message}")
            onComplete(null)
        }
}



