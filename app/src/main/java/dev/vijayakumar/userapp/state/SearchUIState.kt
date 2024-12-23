package dev.vijayakumar.userapp.state

import dev.vijayakumar.userapp.model.response.SearchByUserListResponse

sealed class SearchUIState{

    object Loading : SearchUIState()
    data class Success(val searchByUserListResponse: SearchByUserListResponse):SearchUIState()
    data class Failure(val message : String): SearchUIState()
}