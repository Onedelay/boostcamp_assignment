package com.onedelay.boostcampassignment.movie

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import javax.inject.Inject


internal class MovieAdapter @Inject constructor() : RecyclerView.Adapter<MovieViewHolder>() {

    private val movieLooknFeelList = mutableListOf<MovieLayout.LooknFeel>()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MovieViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.view_holder_movie_item, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.setLooknFeel(movieLooknFeelList[position])
    }

    override fun getItemCount() = movieLooknFeelList.size

    fun setMovieLooknFeelList(list: List<MovieLayout.LooknFeel>) {
        this.movieLooknFeelList.clear()
        this.movieLooknFeelList.addAll(list)

        notifyDataSetChanged()
    }

}