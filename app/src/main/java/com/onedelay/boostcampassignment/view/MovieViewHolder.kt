package com.onedelay.boostcampassignment.view

import android.support.v7.widget.RecyclerView
import com.onedelay.boostcampassignment.databinding.ViewholderItemBinding
import com.onedelay.boostcampassignment.model.MovieItem


class MovieViewHolder(private val binding: ViewholderItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MovieItem) {
        binding.item = item
        binding.executePendingBindings()
    }

}
