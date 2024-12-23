package dev.vijayakumar.userapp.model.response

import dev.vijayakumar.userapp.local.entity.Users

data class UserSearch(
    val firstName: String,
    val lastName: String,
    val email: String
)
