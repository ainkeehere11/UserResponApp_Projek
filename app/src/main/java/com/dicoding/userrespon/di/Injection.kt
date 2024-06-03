package com.dicoding.userrespon.di

import android.content.Context
import com.dicoding.userrespon.AppExecutor
import com.dicoding.userrespon.Repository
import com.dicoding.userrespon.data.retrofit.ApiConfig
import com.dicoding.userrespon.database.FavoriteUserRoomDatabase

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteUserRoomDatabase.getDatabase(context)
        val dao = database.favoriteUserDao()
        val AppExecutor = AppExecutor()
        return Repository.getInstance(apiService, dao, AppExecutor)
    }
}