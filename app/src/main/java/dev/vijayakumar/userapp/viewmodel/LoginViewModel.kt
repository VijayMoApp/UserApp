package dev.vijayakumar.userapp.viewmodel

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vijayakumar.userapp.local.entity.Users
import dev.vijayakumar.userapp.model.response.SearchByUserListResponse
import dev.vijayakumar.userapp.model.response.UserSearch
import dev.vijayakumar.userapp.model.response.UsersListResponse
import dev.vijayakumar.userapp.repository.LocalRepository
import dev.vijayakumar.userapp.repository.LoginRepository
import dev.vijayakumar.userapp.state.FavoriteUIState
import dev.vijayakumar.userapp.state.LoginUiState
import dev.vijayakumar.userapp.state.SearchUIState
import dev.vijayakumar.userapp.state.UsersUIState
import dev.vijayakumar.userapp.view.utils.toUsers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository,
    private val localRepository: LocalRepository
 ) : ViewModel() {

    private val _loginUIState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val loginUIState : MutableStateFlow<LoginUiState> = _loginUIState


    private val _userUIState = MutableStateFlow<UsersUIState>(UsersUIState.Loading)
    val userUIState : MutableStateFlow<UsersUIState> = _userUIState


    private val _searchUIState = MutableStateFlow<SearchUIState>(SearchUIState.Loading)
    val searchUIState :MutableStateFlow<SearchUIState> = _searchUIState


    private val _favoriteUIState = MutableStateFlow<FavoriteUIState>(FavoriteUIState.Loading)
    val favoriteUIState:MutableStateFlow<FavoriteUIState> = _favoriteUIState


    fun loginOperation(username: String, password: String) {
        viewModelScope.launch {

            _loginUIState.value=LoginUiState.Loading
            try {
                val response = loginRepository.loginUser(username,password)
                if(response.isSuccessful && response.body()!=null){
                    _loginUIState.value=LoginUiState.Success(response.body()!!)
                }else{
                    _loginUIState.value=LoginUiState.Failure("Something went wrong")
                }
            }catch (e:Exception){
                _loginUIState.value=LoginUiState.Error         }
        }

    }


    fun getUserOperation() {
        viewModelScope.launch {
            loginRepository.getUsers()
                .onStart {
                    // Emit Loading state
                    _userUIState.value = UsersUIState.Loading
                }
                .catch { e ->
                    // Handle error and emit Failure state
                    _userUIState.value = UsersUIState.Failure(e.message ?: "Unknown error occurred")
                }
                .collect { userList ->
                    // Emit Success state with the user list
                    _userUIState.value = UsersUIState.Success(UsersListResponse(userList))
                }
        }
    }



    fun getUserListByName(query: String) {
        viewModelScope.launch {
            loginRepository.getUserByName(query)
                .onStart {
                    _searchUIState.value = SearchUIState.Loading
                }
                .catch { e ->
                    _searchUIState.value = SearchUIState.Failure(e.message ?: "Unknown error occurred")
                }
                .collect { response ->
                    _searchUIState.value = if (response.users.isEmpty()) {
                        SearchUIState.Failure("No users found")
                    } else {
                        SearchUIState.Success(response)
                    }
                }
        }
    }



    fun insertUsers(userSearch: UserSearch) {
        val userEntity = userSearch.toUsers()
        viewModelScope.launch {
            localRepository.insertUsers(userEntity) // Insert into DB
        }
    }


fun deleteUsers(users: Users) {
    viewModelScope.launch {
        localRepository.deleteUsers(users) // Delete from DB
    }
}

    fun getUsers() {
        viewModelScope.launch {
            localRepository.getUsers()

                .onStart {
                    _favoriteUIState.value = FavoriteUIState.Loading
                }

                .catch {e ->
                    _favoriteUIState.value = FavoriteUIState.Failure(e.message ?: "Unknown error occurred")
                }
                .collect { favorite ->
                   _favoriteUIState.value = FavoriteUIState.Success(favorite)
                }

                }

        }

    }

