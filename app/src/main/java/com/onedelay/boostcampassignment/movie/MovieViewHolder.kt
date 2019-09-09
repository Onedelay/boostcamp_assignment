package com.onedelay.boostcampassignment.movie

import android.support.v7.widget.RecyclerView
import android.view.View
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import kotlinx.android.synthetic.main.view_holder_movie_item.view.*


internal class MovieViewHolder constructor(private val view: View) : RecyclerView.ViewHolder(view) {

    interface ItemClickListener {
        fun onClick(item: MovieLayout.LooknFeel)
        fun onLongClick(item: MovieLayout.LooknFeel)
    }

    private var listener: ItemClickListener? = null

    private lateinit var item: MovieLayout.LooknFeel

    init {
        view.apply {
            setOnClickListener { listener?.onClick(item) }

            setOnLongClickListener {
                listener?.onLongClick(item)

                true
            }
        }
    }

    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    fun setLooknFeel(looknFeel: MovieLayout.LooknFeel) {
        item = looknFeel
        view.custom_fl_movie.setLooknFeel(looknFeel)
    }

    fun updateLikeState(looknFeel: MovieLayout.LooknFeel) {
        view.custom_fl_movie.updateLikedState(looknFeel)
    }

}