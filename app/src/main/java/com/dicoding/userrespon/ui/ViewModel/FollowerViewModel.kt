package com.dicoding.userrespon.ui.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.userrespon.data.response.ItemsItem
import com.dicoding.userrespon.data.retrofit.ApiConfig
import com.dicoding.userrespon.ui.DetailUserActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel : ViewModel() {
    private val _listUserFollower = MutableLiveData<List<ItemsItem>?>()
    val listUserFollower: MutableLiveData<List<ItemsItem>?> = _listUserFollower

    private val _listUserFollowing = MutableLiveData<List<ItemsItem>?>()
    val listUserFollowing: MutableLiveData<List<ItemsItem>?> = _listUserFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _empty = MutableLiveData<Boolean>()
    val empty: LiveData<Boolean> = _empty

    fun findFollowerGithubUser(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _listUserFollower.value = responseBody
                    }
                } else {
                    Log.e(DetailUserActivity.TAG, "onFailure: ${response.message()}")
                    Log.e(DetailUserActivity.TAG, "onFailure: data tidak ditemukan")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(DetailUserActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun findFollowingGithubUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUserFollowing.value = responseBody
                    }
                } else {
                    Log.e(DetailUserActivity.TAG, "onFailure: ${response.message()}")
                    Log.e(DetailUserActivity.TAG, "onFailure: data tidak ditemukan")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(DetailUserActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }
}
