package com.dicoding.userrespon.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.dicoding.userrespon.R
import com.dicoding.userrespon.databinding.ActivityDetailUserBinding
import com.dicoding.userrespon.ui.ViewModel.DetailViewModel
import com.dicoding.userrespon.ui.adapter.SectionPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailUserActivityBinding: ActivityDetailUserBinding

    private val detailViewModel by viewModels<DetailViewModel>()


    companion object {
        const val TAG = "DetailUserActivity"
        const val EXTRA_LOGIN = "login"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"


        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tabFollower,
            R.string.tabFollowing
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailUserActivityBinding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(detailUserActivityBinding.root)

        val username = intent.getStringExtra(EXTRA_LOGIN)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl  = intent.getStringExtra(EXTRA_URL) ?: ""


        val actionBar:ActionBar? = supportActionBar
        actionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (detailViewModel.userDetail.value == null) detailViewModel.findDetail(username.toString())

        detailViewModel.userDetail.observe(this) { userDetailResponse ->
            userDetailResponse?.let { user ->
                with(detailUserActivityBinding) {
                    tvFollowers.text = user.followers.toString()
                    tvFollowing.text = user.following.toString()
                    tvName.text = user.name.toString()
                    tvName2.text = user.login.toString()
                    Glide.with(root)
                        .load(user.avatarUrl)
                        .into(imageView)
                        .clearOnDetach()
                }
            }
        }


        detailViewModel.isLoading.observe(this) {
            detailUserActivityBinding.progressBar.isVisible = it
        }

        val sectionsPagerAdapter = SectionPagerAdapter(this)
        val viewPager = detailUserActivityBinding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = detailUserActivityBinding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()


        var isCheck = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count == true) {
                        detailUserActivityBinding.toggleFavorite.isChecked = true
                        isCheck = true
                    } else {
                        detailUserActivityBinding.toggleFavorite.isChecked = false
                        isCheck = false
                    }
                }
            }
        }
        detailUserActivityBinding.toggleFavorite.setOnClickListener {
            isCheck = !isCheck
            if (isCheck) {
                detailViewModel.insertToFavorite(username.toString(), id, avatarUrl)
            } else {
                detailViewModel.removeFavoriteUser(id)
            }
            detailUserActivityBinding.toggleFavorite.isChecked = isCheck
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}