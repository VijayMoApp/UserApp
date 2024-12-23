package dev.vijayakumar.userapp.state

import dev.vijayakumar.userapp.model.response.UsersListResponse


sealed class UsersUIState {
    object Loading : UsersUIState()
    data class Success(val userListResponse:UsersListResponse) : UsersUIState()
    data class Failure(val message: String) : UsersUIState()

}