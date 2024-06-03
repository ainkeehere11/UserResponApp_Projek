package com.dicoding.userrespon.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.userrespon.entity.FavoriteUser


@Dao
interface FavoriteUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToFavorite(favorite: FavoriteUser)

    @Query("SELECT * FROM favoriteuser where favorite = 1")
    fun getFavorite(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM favoriteuser")
    fun getFavoriteUser() : LiveData<List<FavoriteUser>>

    @Update
    suspend fun updateFavoriteUser(favorite: FavoriteUser)

    @Query("SELECT EXISTS(SELECT * from favoriteuser where id = :id AND favorite = 1)")
    suspend fun cekUser(id: Int): Boolean

    @Query("DELETE FROM favoriteuser WHERE id = :id")
    suspend fun removeFavoriteUser(id: Int)

}