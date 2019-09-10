package com.onedelay.boostcampassignment.movie

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import javax.inject.Inject


internal class MovieAdapter @Inject constructor() : RecyclerView.Adapter<MovieViewHolder>() {

    companion object {
        const val LIKE_UPDATE = "like_update"
    }

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

        if (position == itemCount - threshold) {
            Log.d("MY_LOG", "onLoadCallback")
            adapterListener?.onLoadCallback(itemCount + 1)
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            if (payloads.contains(LIKE_UPDATE)) {
                holder.updateLikeState(movieLooknFeelList[position])
            }
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
        val index = movieLooknFeelList.indexOfFirst { it.link == item.link }

        this.movieLooknFeelList[index].starred = item.starred

        notifyItemChanged(index, LIKE_UPDATE)
    }

    fun makeDifferenceList(list: List<MovieLayout.LooknFeel>): List<MovieLayout.LooknFeel> {
        val previous = this.movieLooknFeelList

        val largerList = mutableListOf<MovieLayout.LooknFeel>()
        val smallerList = mutableListOf<MovieLayout.LooknFeel>()

        if (previous.size < list.size) {
            smallerList.addAll(previous)
            largerList.addAll(list)
        } else {
            smallerList.addAll(list)
            largerList.addAll(previous)
        }

        val diffList = mutableListOf<MovieLayout.LooknFeel>()
        for (i in 0 until largerList.size) {
            if (!smallerList.contains(largerList[i])) {
                diffList.add(largerList[i].apply { starred = false })
            }
        }

        return diffList
    }

}