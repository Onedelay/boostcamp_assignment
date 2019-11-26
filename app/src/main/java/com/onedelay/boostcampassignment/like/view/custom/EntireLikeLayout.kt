package com.onedelay.boostcampassignment.like.view.custom

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.onedelay.boostcampassignment.R
import com.onedelay.boostcampassignment.like.dto.LikeLooknFeel
import com.onedelay.boostcampassignment.movie.MovieAdapter
import com.onedelay.boostcampassignment.movie.MovieViewHolder
import com.onedelay.boostcampassignment.movie.custom.MovieLayout
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_like.view.*
import kotlinx.android.synthetic.main.view_entire_movie.view.*


internal class EntireLikeLayout constructor(
        context: Context,
        attrs: AttributeSet? = null

) : FrameLayout(context, attrs) {

    interface Listener {
        fun onMovieClicked(looknFeel: MovieLayout.LooknFeel)
        fun onLongClick(looknFeel: MovieLayout.LooknFeel)
    }

    @Parcelize
    class LooknFeel(val likedMovieLooknFeelList: MutableList<MovieLayout.LooknFeel>) : Parcelable

    private var looknFeel: LooknFeel? = null

    private var listener: Listener? = null

    private var adapter: MovieAdapter = MovieAdapter()

    init {
        LayoutInflater.from(context).inflate(R.layout.view_entire_like, this, true)

        initializeRecyclerView()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)

            looknFeel = state.looknFeel

            looknFeel?.let(this::setLooknFeel)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val parcelable = super.onSaveInstanceState()

        if (parcelable == null) {
            return parcelable
        }

        return SavedState(parcelable).apply {
            this.looknFeel = this@EntireLikeLayout.looknFeel
        }
    }

    fun setLooknFeel(looknFeel: LooknFeel) {
        this.looknFeel = looknFeel

        adapter.setMovieLooknFeelList(looknFeel.likedMovieLooknFeelList)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun updateLikedMovieList(looknFeel: LikeLooknFeel.BindLikedMovieList) {
        adapter.makeDifferenceList(looknFeel.likedMovieLooknFeelList).forEach {
            adapter.removeItem(it)
        }
    }

    private fun initializeRecyclerView() {
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

        custom_fl_entire_like.rv_movie_list.apply {
            layoutManager = linearLayoutManager

            adapter = this@EntireLikeLayout.adapter

            setHasFixedSize(true)

            addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(context, linearLayoutManager.orientation))
        }

        adapter.apply {
            setListener(object : MovieAdapter.AdapterListener {
                override fun onLoadCallback(position: Int) {
                    // do nothing
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

    private class SavedState : BaseSavedState {
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