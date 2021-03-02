package com.akinci.socialapitrial.common.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.akinci.socialapitrial.databinding.RowShimmerBinding

// randomItem count constructordan gelebilir
// ViewHolder -> ShimmerViewHolder olabilir
class ShimmerAdapter(private val randomItemCount: Int = (2..4).random()) : RecyclerView.Adapter<ShimmerAdapter.ShimmerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ShimmerViewHolder(RowShimmerBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return randomItemCount
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {
        if (holder is ShimmerViewHolder) {
            holder.bind()
        }
    }

    class ShimmerViewHolder(val binding: RowShimmerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.shimmerViewContainer.startShimmer()
        }
    }
}