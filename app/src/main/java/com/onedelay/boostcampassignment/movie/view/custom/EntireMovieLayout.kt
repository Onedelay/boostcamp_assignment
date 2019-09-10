package com.onedelay.boostcampassignment.movie.view.custom

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.movie.MovieAdapter
import com.onedelay.boostcampassignment.movie.MovieViewHolder
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import kotlinx.android.synthetic.main.view_entire_movie.view.*


internal class EntireMovieLayout constructor(
        context: Context,
        attrs: AttributeSet? = null

) : FrameLayout(context, attrs) {

    interface Listener {
        fun onMovieSearchRequested(movieName: String)
        fun onMovieClicked(looknFeel: MovieLayout.LooknFeel)
        fun onLongClick(looknFeel: MovieLayout.LooknFeel)
        fun onLoadMoreRequested(start: Int)
    }

    class LooknFeel(val movieLooknFeelList: List<MovieLayout.LooknFeel>)

    private var looknFeel: LooknFeel? = null

    private var listener: Listener? = null

    private var adapter: MovieAdapter = MovieAdapter()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_entire_movie, this, true)

        initializeRecyclerView()
        initializeListener()
    }

    fun setLooknFeel(looknFeel: LooknFeel) {
        this.looknFeel = looknFeel

        adapter.setMovieLooknFeelList(looknFeel.movieLooknFeelList)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun bindMoreMovieList(list: List<MovieLayout.LooknFeel>) {
        adapter.addMovieLooknFeelList(list)
    }

    fun removeMovieItem(movieItem: MovieLayout.LooknFeel) {
        adapter.removeItem(movieItem)
    }

    fun updateLikedMovieList(likedMovieLooknFeelList: List<MovieLayout.LooknFeel>) {
        likedMovieLooknFeelList.forEach {
            adapter.updateItem(it)
        }
    }

    private fun initializeRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)

        rv_movie_list.apply {
            layoutManager = linearLayoutManager

            adapter = this@EntireMovieLayout.adapter

            setHasFixedSize(true)

            addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
        }

        adapter.apply {
            setListener(object : MovieAdapter.AdapterListener {
                override fun onLoadCallback(position: Int) {
                    listener?.onLoadMoreRequested(start = position)
                }
            })

            setItemClickListener(object : MovieViewHolder.ItemClickListener {
                override fun onClick(item: MovieLayout.LooknFeel) {
                    listener?.onMovieClicked(item)
                }

                override fun onLongClick(item: MovieLayout.LooknFeel) {
                    listener?.onLongClick(item)
                }
            })
        }
    }

    private fun initializeListener() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        b_search.setOnClickListener {
            imm.hideSoftInputFromWindow(et_movie.windowToken, 0)

            listener?.onMovieSearchRequested(movieName = et_movie.text.toString())
        }

        et_movie.setOnEditorActionListener { _, _, _ ->
            imm.hideSoftInputFromWindow(et_movie.windowToken, 0)

            listener?.onMovieSearchRequested(movieName = et_movie.text.toString())

            true
        }
    }

}