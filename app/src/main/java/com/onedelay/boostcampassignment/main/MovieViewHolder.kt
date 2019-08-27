package com.onedelay.boostcampassignment.main

import android.support.v7.widget.RecyclerView
import com.onedelay.boostcampassignment.databinding.ViewholderItemBinding
import com.onedelay.boostcampassignment.data.MovieItem


class MovieViewHolder(private val binding: ViewholderItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MovieItem) {
        binding.item = item
        binding.executePendingBindings()
    }

}
