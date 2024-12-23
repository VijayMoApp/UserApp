package dev.vijayakumar.userapp.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.vijayakumar.userapp.local.dao.UsersDAO
import dev.vijayakumar.userapp.local.entity.Users


@Database([Users::class], version = 1, exportSchema = false)
abstract class UserDatabase():RoomDatabase(){
    abstract fun getUserDao(): UsersDAO
}