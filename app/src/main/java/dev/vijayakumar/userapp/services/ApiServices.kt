package dev.vijayakumar.userapp.services

import dev.vijayakumar.userapp.model.request.LoginRequest
import dev.vijayakumar.userapp.model.response.LoginResponse
import dev.vijayakumar.userapp.model.response.SearchByUserListResponse
import dev.vijayakumar.userapp.model.response.UsersListResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.Flow

interface ApiServices {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>


    @GET("users")
    suspend fun getUserList() : UsersListResponse


    @GET("users/search")
    suspend fun searchByName(@Query("q") query:String) :SearchByUserListResponse

}