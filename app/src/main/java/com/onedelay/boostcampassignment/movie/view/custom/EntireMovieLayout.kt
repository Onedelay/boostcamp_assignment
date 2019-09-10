package com.onedelay.boostcampassignment.movie.view.custom

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.movie.MovieAdapter
import com.onedelay.boostcampassignment.movie.MovieViewHolder
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.view_entire_movie.view.*


internal class EntireMovieLayout constructor(
        context: Context,
        attrs: AttributeSet? = null

) : FrameLayout(context, attrs) {

    interface Listener {
        fun onMovieSearchRequested(movieName: String)
        fun onMovieClicked(looknFeel: MovieLayout.LooknFeel)
        fun onLongClick(looknFeel: MovieLayout.LooknFeel)
        fun onLoadMoreRequested(movieName: String, start: Int)
    }

    @Parcelize
    class LooknFeel(val movieLooknFeelList: MutableList<MovieLayout.LooknFeel>) : Parcelable

    private var looknFeel: LooknFeel? = null

    private var listener: Listener? = null

    private var adapter: MovieAdapter = MovieAdapter()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_entire_movie, this, true)

        initializeRecyclerView()
        initializeListener()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if(state is SavedState) {
            super.onRestoreInstanceState(state.superState)

            looknFeel = state.looknFeel

            looknFeel?.let(this::setLooknFeel)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val parcelable = super.onSaveInstanceState()

        if(parcelable == null) {
            return parcelable
        }

        return SavedState(parcelable).apply {
            this.looknFeel = this@EntireMovieLayout.looknFeel
        }
    }

    fun setLooknFeel(looknFeel: LooknFeel) {
        this.looknFeel = looknFeel

        adapter.setMovieLooknFeelList(looknFeel.movieLooknFeelList)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun bindMoreMovieList(list: List<MovieLayout.LooknFeel>) {
        looknFeel!!.movieLooknFeelList.addAll(list)

        adapter.addMovieLooknFeelList(list)
    }

    fun removeMovieItem(movieItem: MovieLayout.LooknFeel) {
        looknFeel!!.movieLooknFeelList.removeIf { it.link == movieItem.link }

        adapter.removeItem(movieItem)
    }

    fun updateLikedMovieList(likedMovieLooknFeelList: List<MovieLayout.LooknFeel>) {
        likedMovieLooknFeelList.forEach { likedMovie ->
            looknFeel!!.movieLooknFeelList.first { movie -> movie.link == likedMovie.link }.starred = likedMovie.starred

            adapter.updateItem(likedMovie)
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
                    listener?.onLoadMoreRequested(movieName = et_movie.text.toString(), start = position)
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

    private class SavedState : View.BaseSavedState {
        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }

        var looknFeel: LooknFeel? = null

        constructor(superState: Parcelable) : super(superState)

        private constructor(source: Parcel) : super(source) {
            this.looknFeel = source.readParcelable(LooknFeel::class.java.classLoader)
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)

            out.writeParcelable(looknFeel, flags)
        }
    }

}