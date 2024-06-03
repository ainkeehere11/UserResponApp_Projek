package com.dicoding.userrespon.ui.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.userrespon.MainActivity
import com.dicoding.userrespon.data.response.GithubResponse
import com.dicoding.userrespon.data.response.ItemsItem
import com.dicoding.userrespon.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUserGithub = MutableLiveData<List<ItemsItem>>()
    val listUserGithub: LiveData<List<ItemsItem>> = _listUserGithub

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _empty = MutableLiveData<Boolean>()
    val empty: LiveData<Boolean> = _empty

    init {
        findGithubUser()
    }

    fun findGithubUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGithubResponse(MainActivity.GITHUB_LOGIN)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        _listUserGithub.value = it.items
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
