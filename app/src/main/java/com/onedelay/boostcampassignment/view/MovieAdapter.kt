package com.onedelay.boostcampassignment.view

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.databinding.ViewholderItemBinding
import com.onedelay.boostcampassignment.model.MovieItem
import java.util.*

class MovieAdapter(private val listener: OnMovieListener) : RecyclerView.Adapter<MovieViewHolder>() {
    private val list = ArrayList<MovieItem>()

    private val threshold = 5

    interface OnMovieListener {
        fun onMovieItemClick(item: MovieItem)
        fun onLoadMoreMovieList(position: Int)  // 데이터 추가 요청(페이징)
    }

    fun addItems(list: List<MovieItem>) {
        val prevSize = this.list.size
        this.list.addAll(list)
        notifyItemRangeInserted(prevSize, list.size)
    }

    fun clearItems() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = DataBindingUtil.inflate<ViewholderItemBinding>(LayoutInflater.from(viewGroup.context), R.layout.viewholder_item, viewGroup, false)
        binding.listener = listener
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        // 현재 position 이 (데이터 개수 + 1 - threshold) 일 경우
        // ex. 30 + 1 - 5 = 26 -> request(31) : start = 31
        if (position == itemCount - threshold) {
            listener.onLoadMoreMovieList(itemCount + 1)
        }
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}
