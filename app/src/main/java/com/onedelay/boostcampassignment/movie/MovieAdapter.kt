package com.onedelay.boostcampassignment.movie

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import javax.inject.Inject


internal class MovieAdapter @Inject constructor() : RecyclerView.Adapter<MovieViewHolder>() {

    interface AdapterListener {
        fun onLoadCallback(position: Int)
    }

    private val movieLooknFeelList = mutableListOf<MovieLayout.LooknFeel>()

    private var adapterListener: AdapterListener? = null

    private var itemListener: MovieViewHolder.ItemClickListener? = null

    private val threshold = 5

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MovieViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie_item, parent, false)

        return MovieViewHolder(view).apply {
            setItemClickListener(itemListener)
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.setLooknFeel(movieLooknFeelList[position])

        if(position == itemCount - threshold) {
            adapterListener?.onLoadCallback(itemCount + 1)
        }
    }

    override fun getItemCount() = movieLooknFeelList.size

    fun setListener(adapterListener: AdapterListener) {
        this.adapterListener = adapterListener
    }

    fun setItemClickListener(listener: MovieViewHolder.ItemClickListener) {
        this.itemListener = listener
    }

    fun setMovieLooknFeelList(list: List<MovieLayout.LooknFeel>) {
        this.movieLooknFeelList.clear()
        this.movieLooknFeelList.addAll(list)

        notifyDataSetChanged()
    }

    fun addMovieLooknFeelList(list: List<MovieLayout.LooknFeel>) {
        val prevSize = this.movieLooknFeelList.size

        this.movieLooknFeelList.addAll(list)

        notifyItemRangeInserted(prevSize, list.size)
    }

    fun removeItem(item: MovieLayout.LooknFeel) {
        val index = movieLooknFeelList.indexOfFirst { it.link == item.link }

        this.movieLooknFeelList.removeAt(index)

        notifyItemRemoved(index)
    }

    fun updateItem(item: MovieLayout.LooknFeel) {
        val position = movieLooknFeelList.indexOfFirst { it.link == item.link }

        this.movieLooknFeelList[position].starred = !item.starred

        notifyItemChanged(position)
    }

}