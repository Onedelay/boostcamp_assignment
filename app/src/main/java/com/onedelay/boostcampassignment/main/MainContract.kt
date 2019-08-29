package com.onedelay.boostcampassignment.main

import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel


internal interface MainContract {

    interface View {

        fun showToastMessage(message: String)

        fun showMovieList(list: List<MovieItemLookFeel>)

        fun showProgressBar()

        fun showResult()

        fun showEmptyResult()

        fun removeMovieItem(item: MovieItemLookFeel)

    }

    interface Presenter {

        fun onDestroy()

        fun checkNetworkStatus(status: Boolean): Boolean

        fun requestMovies(query: String)

        fun loadMoreMovies(position: Int)

        fun addLikedMovie(item: MovieItemLookFeel)

        fun selectDialogMenuOf(item: MovieItemLookFeel, which: Int)

    }

}