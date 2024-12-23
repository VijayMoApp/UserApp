package dev.vijayakumar.userapp.repository

import dev.vijayakumar.userapp.local.dao.UsersDAO
import dev.vijayakumar.userapp.local.entity.Users
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepository @Inject constructor(val usersDAO: UsersDAO) {


    suspend fun insertUsers(users: Users) {
         usersDAO.insertUsers(users)
    }

    fun getUsers(): Flow<List<Users>> {
        return usersDAO.getUsers()
    }

    suspend fun deleteUsers(users: Users) {
        usersDAO.deleteUsers(users)

    }
}