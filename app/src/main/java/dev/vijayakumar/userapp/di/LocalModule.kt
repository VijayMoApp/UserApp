package dev.vijayakumar.userapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.vijayakumar.userapp.local.dao.UsersDAO
import dev.vijayakumar.userapp.local.database.UserDatabase

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user_db"
        ).build()
    }


    @Provides
    @Singleton
    fun provideDao(userDatabase: UserDatabase) : UsersDAO {
        return userDatabase.getUserDao()
    }
}