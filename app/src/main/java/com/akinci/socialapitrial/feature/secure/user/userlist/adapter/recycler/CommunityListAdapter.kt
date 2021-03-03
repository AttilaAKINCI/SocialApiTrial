package com.akinci.socialapitrial.feature.secure.user.userlist.adapter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akinci.socialapitrial.common.component.GlideApp
import com.akinci.socialapitrial.databinding.RowUserBinding
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse

// TODO UserViewHolder ile degistirebilirsin holderi
class CommunityListAdapter(private val clickListener: (Long, String, String) -> Unit) : ListAdapter<UserResponse, RecyclerView.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserViewHolder(RowUserBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if(holder is UserViewHolder) { holder.bind(item, clickListener) }
    }

    class UserViewHolder(val binding: RowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UserResponse, clickListener: (Long, String, String) -> Unit) {
            // fill row instances..
            binding.userCardView.setOnClickListener { clickListener.invoke(data.id, data.screen_name, data.name) }
            binding.data = data

            data.profile_image_url_https?.let {
                //Bunu kullanimi baska yerlerde de var, extension yapabilirsin
                GlideApp.with(binding.userImage.context)
                        .load(it)
                        .centerCrop()
                        .into(binding.userImage)
            }

            binding.executePendingBindings()
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<UserResponse>() {
        override fun areItemsTheSame(oldItem: UserResponse, newItem: UserResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserResponse, newItem: UserResponse): Boolean {
            return oldItem == newItem
        }
    }
}
