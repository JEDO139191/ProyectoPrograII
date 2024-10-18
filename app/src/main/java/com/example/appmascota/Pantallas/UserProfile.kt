package com.example.appmascota.Pantallas

import android.net.Uri

data class UserProfile(
    val PetName: String = "",
    val PetAge: String = "",
    val PetBreed: String = "",
    val PetGender: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val profilePicUri: String? = null
)
