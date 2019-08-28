package com.onedelay.boostcampassignment.liked

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.data.MovieItem
import com.onedelay.boostcampassignment.main.MovieViewHolder
import java.util.*


internal class LikedMovieAdapter constructor(
        private val listener: MovieViewHolder.ItemClickListener)

    : RecyclerView.Adapter<MovieViewHolder>() {

    private val list = ArrayList<MovieItem>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.viewholder_item, viewGroup, false)

        return MovieViewHolder(view).apply {
            setItemClickListener(listener)
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun addItems(list: List<MovieItem>) {
        val prevSize = this.list.size
        this.list.addAll(list)
        notifyItemRangeInserted(prevSize, list.size)
    }

    fun removeItem(item: MovieItem) {
        val index = list.indexOf(item)
        list.remove(item)
        notifyItemRemoved(index)
    }

    fun clearItems() {
        list.clear()
        notifyDataSetChanged()
    }

}
