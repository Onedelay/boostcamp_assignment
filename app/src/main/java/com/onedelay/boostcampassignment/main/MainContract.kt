package com.onedelay.boostcampassignment.main

import com.onedelay.boostcampassignment.data.MovieItem


internal interface MainContract {

    interface View {

        fun showToastMessage(message: String)

        fun showMovieList(list: List<MovieItem>)

        fun showProgressBar()

        fun showResult()

        fun showEmptyResult()

        fun removeMovieItem(item: MovieItem)

    }

    interface Presenter {

        fun onDestroy()

        fun checkNetworkStatus(status: Boolean): Boolean

        fun requestMovies(query: String)

        fun loadMoreMovies(position: Int)

        fun addLikedMovie(item: MovieItem)

        fun selectDialogMenuOf(item: MovieItem, which: Int)

    }

}