package com.akinci.socialapitrial.feature.secure.user.userdetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akinci.socialapitrial.databinding.RowTimeLineBinding
import com.akinci.socialapitrial.feature.secure.user.data.output.userdetail.UserTimeLineResponse

//TODO direk holder i pass edip castingi silebilirsin
class TimeLineListAdapter : ListAdapter<UserTimeLineResponse, TimeLineListAdapter.UserTimeLineViewHolder>(UserTimeLineItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTimeLineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserTimeLineViewHolder(RowTimeLineBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: UserTimeLineViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class UserTimeLineViewHolder(val binding: RowTimeLineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UserTimeLineResponse) {
            // fill row instances..
            binding.data = data

            //TODO var yerine kotlin listlerin extensionlarini kullanabilirsin
            // Trim Start a da gerek kalmaz
            // Hashtag listesi null gelmiyor gibi sanki hic, empty list geliyor
            val hashTags: String = if (data.entities?.hashtags?.isNotEmpty() == true) {
                data.entities.hashtags.joinToString(separator = "#", prefix = "#") {
                    "${it.text} "
                }
            } else ""

            binding.hashTags.visibility = if (hashTags.isNotEmpty()) View.VISIBLE else View.GONE
            binding.executePendingBindings()
        }
    }

    class UserTimeLineItemDiffCallback : DiffUtil.ItemCallback<UserTimeLineResponse>() {
        override fun areItemsTheSame(oldItem: UserTimeLineResponse, newItem: UserTimeLineResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserTimeLineResponse, newItem: UserTimeLineResponse): Boolean {
            return oldItem == newItem
        }
    }
}
