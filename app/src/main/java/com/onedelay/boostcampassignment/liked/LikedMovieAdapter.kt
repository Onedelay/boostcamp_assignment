package com.onedelay.boostcampassignment.liked

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel
import com.onedelay.boostcampassignment.main.MovieViewHolder
import java.util.*


internal class LikedMovieAdapter : RecyclerView.Adapter<MovieViewHolder>() {

    private val list = ArrayList<MovieItemLookFeel>()

    private var listener: MovieViewHolder.ItemClickListener? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.view_movie_item, viewGroup, false)

        return MovieViewHolder(view).apply {
            setItemClickListener(listener)
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if(holder.adapterPosition != RecyclerView.NO_POSITION) {
            holder.bind(list[position])
        }
    }

    override fun getItemCount() = list.size

    fun setListener(listener: MovieViewHolder.ItemClickListener) {
        this.listener = listener
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

}
