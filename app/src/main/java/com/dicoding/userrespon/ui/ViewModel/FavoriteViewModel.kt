package com.dicoding.userrespon.ui.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.dicoding.userrespon.database.FavoriteUserRoomDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    fun getFavoriteData(context: Context) =
        FavoriteUserRoomDatabase.getDatabase(context.applicationContext).favoriteUserDao().getFavoriteUser()
}