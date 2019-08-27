package com.onedelay.boostcampassignment.main

import com.onedelay.boostcampassignment.data.MovieItem


internal interface MainContract {

    interface View {

        fun showErrorMessage(message: String)

        fun showMovieList(list: List<MovieItem>)

        fun showProgressBar()

        fun showResult()

        fun showEmptyResult()

    }

    interface Presenter {

        fun onDestroy()

        fun checkNetworkStatus(status: Boolean): Boolean

        fun requestMovies(query: String)

        fun loadMoreMovies(position: Int)

    }

}