package dev.vijayakumar.userapp.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import dev.vijayakumar.userapp.model.request.LoginRequest
import dev.vijayakumar.userapp.model.response.LoginResponse
import dev.vijayakumar.userapp.model.response.SearchByUserListResponse
import dev.vijayakumar.userapp.model.response.User
import dev.vijayakumar.userapp.services.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class LoginRepository @Inject constructor(private val apiService: ApiServices)  {


    suspend fun loginUser(username: String, password: String): Response<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.login(LoginRequest(username, password))
            }catch (e:Exception){
                throw Exception("Network Error")
            }
        }
    }



    fun getUsers():Flow<List<User>> = flow {
        try {
            val response = apiService.getUserList()
            emit(response.users) // Emit only the list of users.
        } catch (e: Exception) {
            emit(emptyList()) // Emit empty list on error to avoid crash.
        }
    }



    fun getUserByName(name: String): Flow<SearchByUserListResponse> = flow {
        val response = apiService.searchByName(name)
        emit(response)
    }.catch { e ->
        emit(SearchByUserListResponse()) // Emit an empty list on error
    }


}