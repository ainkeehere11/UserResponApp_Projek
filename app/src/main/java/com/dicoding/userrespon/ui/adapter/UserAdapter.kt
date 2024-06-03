package com.dicoding.userrespon.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.userrespon.data.response.ItemsItem
import com.dicoding.userrespon.databinding.ItemUserBinding
import com.dicoding.userrespon.ui.DetailUserActivity

class UserAdapter : ListAdapter<ItemsItem, UserAdapter.UserViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }


    class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            with(binding) {
                tvUsername.text = user.login
                Glide.with(root)
                    .load(user.avatarUrl)
                    .into(ivUser)
                    .clearOnDetach()
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailUserActivity::class.java).apply {
                    putExtra(DetailUserActivity.EXTRA_LOGIN, user.login)
                    putExtra(DetailUserActivity.EXTRA_ID, user.id)
                    putExtra(DetailUserActivity.EXTRA_URL, user.avatarUrl)
                }
                itemView.context.startActivity(intent)
            }
        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
