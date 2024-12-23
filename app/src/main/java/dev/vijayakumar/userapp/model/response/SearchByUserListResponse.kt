package dev.vijayakumar.userapp.model.response

data class SearchByUserListResponse(
    val users: List<User> = emptyList()
)