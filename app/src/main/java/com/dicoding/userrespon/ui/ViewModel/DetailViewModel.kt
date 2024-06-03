package com.dicoding.userrespon.ui.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.userrespon.data.response.DetailUserResponse
import com.dicoding.userrespon.data.retrofit.ApiConfig
import com.dicoding.userrespon.database.FavoriteUserDao
import com.dicoding.userrespon.database.FavoriteUserRoomDatabase
import com.dicoding.userrespon.entity.FavoriteUser
import com.dicoding.userrespon.ui.DetailUserActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    val userDetail = MutableLiveData<DetailUserResponse?>()

    private var favoriteUserDao: FavoriteUserDao?
    private var userDb: FavoriteUserRoomDatabase?

    init {
        userDb = FavoriteUserRoomDatabase.getDatabase(application)
        favoriteUserDao = userDb?.favoriteUserDao()
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun findDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        userDetail.value = responseBody
                    }
                } else {
                    Log.e(DetailUserActivity.TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(DetailUserActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }


    fun insertToFavorite(login: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(
                id,
                login,
                avatarUrl,
                isFavoriteUser = true
            )
            favoriteUserDao?.insertToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = favoriteUserDao?.cekUser(id)
    fun removeFavoriteUser(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteUserDao?.removeFavoriteUser(id)
        }
    }

}