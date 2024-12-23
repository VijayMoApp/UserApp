package dev.vijayakumar.userapp.view.utils

import dev.vijayakumar.userapp.local.entity.Users
import dev.vijayakumar.userapp.model.response.UserSearch

fun UserSearch.toUsers(): Users {
    return Users(
        id = 0, // Assign a proper ID if needed
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email
    )
}