package com.dicoding.userrespon


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.userrespon.data.response.DetailUserResponse
import com.dicoding.userrespon.data.response.GithubResponse
import com.dicoding.userrespon.data.response.ItemsItem
import com.dicoding.userrespon.data.retrofit.ApiConfig
import com.dicoding.userrespon.data.retrofit.ApiService
import com.dicoding.userrespon.database.FavoriteUserDao
import com.dicoding.userrespon.entity.FavoriteUser
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class Repository private constructor(
    private val apiService: ApiService,
    private val favoriteUserDao: FavoriteUserDao,
    private val AppExecutor: AppExecutor
){
    private val result = MediatorLiveData<Result<List<ItemsItem>>>()
    private val resultdetail = MediatorLiveData<Result<DetailUserResponse>>()

    fun findUserGithub(query: String): LiveData<Result<List<ItemsItem>>> {
        result.value = Result.Loading
        val client = apiService.getGithubResponse(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ){
                if (response.isSuccessful) {
                    response.body()?.let {
                        val totalCount = it.totalCount!!
                        result.value=
                            if (totalCount < 1) Result.Empty else Result.Success(it.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}" )
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return result
    }

    fun findDetailGithub(query: String): LiveData<Result<DetailUserResponse>>{
        resultdetail.value = Result.Loading
        val client = ApiConfig.getApiService().getDetailUser(query)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse (
                call: Call<DetailUserResponse>, response: Response<DetailUserResponse>
            ){
                if (response.isSuccessful) {
                    resultdetail.value = Result.Success(response.body()!!)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                resultdetail.value = Result.Error(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return resultdetail
    }

    fun findFollower(query: String): LiveData<Result<List<ItemsItem>>> {
        result.value = Result.Loading
        val client = ApiConfig.getApiService().getFollowers(query)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ){
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body()!!)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return result
    }

    fun findFollowing(query: String): LiveData<Result<List<ItemsItem>>> {
        result.value = Result.Loading
        val client = ApiConfig.getApiService().getFollowers(query)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ){
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body()!!)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return result
    }

    fun findFavorite(): LiveData<List<FavoriteUser>> {
        return favoriteUserDao.getFavorite()
    }

    suspend fun setUserFavorite(user: FavoriteUser, favoriteState: Boolean) {
        user.isFavoriteUser = favoriteState
        favoriteUserDao.updateFavoriteUser(user)
    }

    companion object {
        const val TAG = "Repository"

        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteUserDao: FavoriteUserDao,
            AppExecutor: AppExecutor
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, favoriteUserDao, AppExecutor)
            }.also { instance = it }
    }
}