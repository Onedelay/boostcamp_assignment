package com.onedelay.boostcampassignment.view

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.databinding.ViewholderItemBinding
import com.onedelay.boostcampassignment.model.MovieItem
import java.util.*


internal class MovieAdapter constructor(private val listener: OnMovieListener) : RecyclerView.Adapter<MovieViewHolder>() {

    interface OnMovieListener {
        fun onMovieItemClick(item: MovieItem)
        fun onLoadMoreMovieList(position: Int)
    }

    private val list = ArrayList<MovieItem>()

    private val threshold = 5

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = DataBindingUtil.inflate<ViewholderItemBinding>(LayoutInflater.from(viewGroup.context), R.layout.viewholder_item, viewGroup, false)

        binding.listener = listener

        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        /** 현재 position 이 (데이터 개수 + 1 - threshold) 일 경우
         * ex. 30 + 1 - 5 = 26 -> request(31) : start = 31
         * 26 번째 위치에 있을 때 31 번째 데이터 요청
         */
        if(position == itemCount - threshold) {
            listener.onLoadMoreMovieList(itemCount + 1)
        }

        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun addItems(list: List<MovieItem>) {
        val prevSize = this.list.size
        this.list.addAll(list)
        notifyItemRangeInserted(prevSize, list.size)
    }

    fun clearItems() {
        list.clear()
        notifyDataSetChanged()
    }

}
