package com.akinci.socialapitrial.feature.secure.user.userdetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akinci.socialapitrial.databinding.RowTimeLineBinding
import com.akinci.socialapitrial.feature.secure.user.data.output.userdetail.UserTimeLineResponse

class TimeLineListAdapter : ListAdapter<UserTimeLineResponse, RecyclerView.ViewHolder>(UserTimeLineItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserTimeLineViewHolder(RowTimeLineBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if(holder is UserTimeLineViewHolder) { holder.bind(item) }
    }

    class UserTimeLineViewHolder(val binding: RowTimeLineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UserTimeLineResponse) {
            // fill row instances..
            binding.data = data

            var hashTags = ""
            data.entities?.hashtags?.map {
                hashTags = "$hashTags #${it.text}"
            }
            if(hashTags.isNotEmpty()){
                binding.hashTags.visibility = View.VISIBLE
                binding.hashTags.text = hashTags.trimStart()
            }else{
                binding.hashTags.visibility = View.GONE
            }

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
