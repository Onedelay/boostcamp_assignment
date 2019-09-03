package com.onedelay.boostcampassignment.movie

import android.support.v7.widget.RecyclerView
import android.view.View
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import kotlinx.android.synthetic.main.view_holder_movie_item.view.*


internal class MovieViewHolder constructor(private val view: View) : RecyclerView.ViewHolder(view) {

    fun setLooknFeel(looknFeel: MovieLayout.LooknFeel) {
        view.custom_fl_movie.setLooknFeel(looknFeel)
    }

}