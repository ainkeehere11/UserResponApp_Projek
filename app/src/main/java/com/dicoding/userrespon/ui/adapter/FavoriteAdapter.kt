package com.dicoding.userrespon.ui.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.dicoding.userrespon.entity.FavoriteUser
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.userrespon.databinding.ItemUserBinding
import com.dicoding.userrespon.ui.DetailUserActivity

class FavoriteAdapter : ListAdapter<FavoriteUser, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val bindingUser = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(bindingUser)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(private val bindingUserItem: ItemUserBinding) : RecyclerView.ViewHolder(bindingUserItem.root) {
        fun bind(user: FavoriteUser) {
            with(bindingUserItem) {
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteUser>() {
            override fun areItemsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }
        }
    }
}