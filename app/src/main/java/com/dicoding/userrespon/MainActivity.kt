package com.dicoding.userrespon

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.userrespon.data.response.ItemsItem
import com.dicoding.userrespon.databinding.ActivityMainBinding
import com.dicoding.userrespon.ui.FavoriteActivity
import com.dicoding.userrespon.ui.ViewModel.MainViewModel
import com.dicoding.userrespon.ui.adapter.UserAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var mainActitivityBinding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    companion object {
        var GITHUB_LOGIN = "a"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActitivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActitivityBinding.root)

        val layoutManager = LinearLayoutManager(this)
        mainActitivityBinding.recyclerView.layoutManager = layoutManager

        viewModel.apply {
            listUserGithub.observe(this@MainActivity) { listUser ->
                setAdapter(listUser)
            }
            isLoading.observe(this@MainActivity) { isLoading ->
                mainActitivityBinding.progressBar.isVisible = isLoading
            }
            empty.observe(this@MainActivity) { isEmpty ->
                mainActitivityBinding.empty.isVisible = isEmpty
            }
        }


        with(mainActitivityBinding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    //searchBar.text = searchView.text
                    searchView.hide()
                    GITHUB_LOGIN = searchView.text.toString()
                    viewModel.findGithubUser()
                    false
                }
            fab.setOnClickListener {
                startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(this@MainActivity, SwitchThemeActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setAdapter(listUser: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(listUser)
        mainActitivityBinding.recyclerView.adapter = adapter
    }

}



