package com.dicoding.userrespon.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.userrespon.databinding.ActivityFavoriteBinding
import com.dicoding.userrespon.entity.FavoriteUser
import com.dicoding.userrespon.ui.ViewModel.FavoriteViewModel
import com.dicoding.userrespon.ui.adapter.FavoriteAdapter

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteBinding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        val actionBar = supportActionBar
        actionBar?.title = "favorite user"

        loading(true)
        showFavorite()
    }

    private fun loading(isLoading: Boolean) {
        favoriteBinding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setDataUser(list: List<FavoriteUser>) {
        val newAdapter = FavoriteAdapter()
        newAdapter.submitList(list)

        favoriteBinding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newAdapter
        }

    }

    override fun onResume() {
        super.onResume()
        showFavorite()
    }

    private fun showFavorite() {
        favoriteViewModel.getFavoriteData(this).observe(this@FavoriteActivity) { listUser ->
            Log.d("ListFavorite", listUser.toString())
            loading(false)
            val isEmpty = listUser.isNullOrEmpty()
            setDataUser(listUser)
            nonFavorite(isEmpty)
        }


    }

    fun nonFavorite(state: Boolean) {
        favoriteBinding.empty2.visibility = if (state) View.VISIBLE else View.GONE
    }
}