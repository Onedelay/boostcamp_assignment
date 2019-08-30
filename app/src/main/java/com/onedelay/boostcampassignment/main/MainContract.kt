package com.onedelay.boostcampassignment.main

import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel
import java.lang.ref.WeakReference


internal interface MainContract {
    interface View {
        fun showToastMessage(message: String)
        fun showMovieList(list: List<MovieItemLookFeel>)
        fun showProgressBar()
        fun showResult()
        fun showEmptyResult()
        fun removeMovieItem(item: MovieItemLookFeel)
        fun notifyUpdateListItem(item: MovieItemLookFeel)
        fun notifyUpdateList(list: List<MovieItemLookFeel>)
    }

    interface Presenter {
        fun checkNetworkStatus(status: Boolean): Boolean
        fun requestMovies(query: String)
        fun loadMoreMovies(position: Int)
        fun updateLikedMovie(item: MovieItemLookFeel)
        fun selectDialogMenuOf(item: MovieItemLookFeel, which: Int)
        fun notifyChangedLikedMovieList()
    }
}