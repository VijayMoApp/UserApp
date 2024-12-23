package dev.vijayakumar.userapp.state

import dev.vijayakumar.userapp.model.response.LoginResponse

sealed class LoginUiState  {
    object Loading : LoginUiState()
    data class Success(val loginResponse: LoginResponse) : LoginUiState()
    data class Failure(val message: String) : LoginUiState()
    object Error : LoginUiState()
}