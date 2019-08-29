package com.onedelay.boostcampassignment.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel
import java.util.*


internal class MovieAdapter constructor(
        private val listener: MovieViewHolder.ItemClickListener,
        private val onLoadMoreMovies: (Int) -> Unit)

    : RecyclerView.Adapter<MovieViewHolder>() {

    private val list = ArrayList<MovieItemLookFeel>()

    private val threshold = 5

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.viewholder_item, viewGroup, false)

        return MovieViewHolder(view).apply {
            setItemClickListener(listener)
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        /** 현재 position 이 (데이터 개수 + 1 - threshold) 일 경우
         * ex. 30 + 1 - 5 = 26 -> request(31) : start = 31
         * 26 번째 위치에 있을 때 31 번째 데이터 요청
         */
        if(position == itemCount - threshold) {
            onLoadMoreMovies(itemCount + 1)
        }

        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun addItems(list: List<MovieItemLookFeel>) {
        val prevSize = this.list.size
        this.list.addAll(list)
        notifyItemRangeInserted(prevSize, list.size)
    }

    fun removeItem(item: MovieItemLookFeel) {
        val index = list.indexOf(item)
        list.remove(item)
        notifyItemRemoved(index)
    }

    fun clearItems() {
        list.clear()
        notifyDataSetChanged()
    }

    fun updateItem(item: MovieItemLookFeel) {
        val position = list.indexOf(item)
        list[position].starred = item.starred
        notifyItemChanged(position)
    }

}
