package dev.vijayakumar.userapp.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.vijayakumar.userapp.local.entity.Users
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users:Users)

    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<Users>>


    @Delete
    suspend fun deleteUsers(users: Users)

}