package dev.vijayakumar.userapp.state

import dev.vijayakumar.userapp.local.entity.Users

sealed class FavoriteUIState {

    object Loading : FavoriteUIState()
    data class Success(val users: List<Users>) : FavoriteUIState()
    data class Failure(val errorMessage: String) : FavoriteUIState()
    object Error : FavoriteUIState()

}