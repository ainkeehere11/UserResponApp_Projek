package com.dicoding.userrespon.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.userrespon.data.response.ItemsItem
import com.dicoding.userrespon.databinding.FragmentFollowersBinding
import com.dicoding.userrespon.ui.DetailUserActivity
import com.dicoding.userrespon.ui.ViewModel.FollowerViewModel
import com.dicoding.userrespon.ui.adapter.UserAdapter

class FollowFragment : Fragment() {
    private val viewModel by viewModels<FollowerViewModel>()
    private lateinit var fragmentFollowersBinding: FragmentFollowersBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentFollowersBinding = FragmentFollowersBinding.inflate(inflater, container, false)
        return fragmentFollowersBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val count = arguments?.getInt(ARG_NUMBER, 0)
        val username = requireActivity().intent.getStringExtra(DetailUserActivity.EXTRA_LOGIN)

        val layoutManager = LinearLayoutManager(context)
        fragmentFollowersBinding.rvFollowers.layoutManager = layoutManager

        viewModel.isLoading.observe(viewLifecycleOwner) {
            fragmentFollowersBinding.progressBar.isVisible = it
        }
        viewModel.empty.observe(viewLifecycleOwner) {
            fragmentFollowersBinding.Empty.isVisible = it
        }
        if (count == 1) {
            if (viewModel.listUserFollower.value == null) viewModel.findFollowerGithubUser(username.toString())
            viewModel.listUserFollower.observe(viewLifecycleOwner) {
                it?.let { setFollowData(it) }
            }
        } else {
            if (viewModel.listUserFollowing.value == null) viewModel.findFollowingGithubUser(username.toString())
            viewModel.listUserFollowing.observe(viewLifecycleOwner) {
                it?.let { setFollowData(it) }
            }
        }
    }

    companion object {
        const val ARG_NUMBER = "section"
    }

    private fun setFollowData(user: List<ItemsItem>?) {
        val adapter = UserAdapter()
        adapter.submitList(user)
        fragmentFollowersBinding.rvFollowers.adapter = adapter
    }

}
