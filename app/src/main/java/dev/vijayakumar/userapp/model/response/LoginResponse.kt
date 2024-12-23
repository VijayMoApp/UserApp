package dev.vijayakumar.userapp.model.response

import android.os.Parcelable


data class LoginResponse(
    val accessToken: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val id: Int,

)