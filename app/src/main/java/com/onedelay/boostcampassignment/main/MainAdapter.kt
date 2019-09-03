package com.onedelay.boostcampassignment.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel
import java.util.*


internal class MainAdapter : RecyclerView.Adapter<MovieViewHolder>() {

    private val list = ArrayList<MovieItemLookFeel>()

    private val threshold = 5

    private var listener: MovieViewHolder.ItemClickListener? = null

    private var loadMoreMoviesCallback: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.view_movie_item, viewGroup, false)

        return MovieViewHolder(view).apply {
            if(listener != null) {
                setItemClickListener(listener)
            }
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        /** 현재 position 이 (데이터 개수 + 1 - threshold) 일 경우
         * ex. 30 + 1 - 5 = 26 -> request(31) : start = 31
         * 26 번째 위치에 있을 때 31 번째 데이터 요청
         */
        if(position == itemCount - threshold) {
            loadMoreMoviesCallback?.invoke(itemCount + 1)
        }

        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun setListener(listener: MovieViewHolder.ItemClickListener) {
        this.listener = listener
    }

    fun setLoadMoreMoviesCallback(callback: (Int) -> Unit) {
        this.loadMoreMoviesCallback = callback
    }

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
