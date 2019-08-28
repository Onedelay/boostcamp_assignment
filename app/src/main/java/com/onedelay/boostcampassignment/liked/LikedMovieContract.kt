package com.onedelay.boostcampassignment.liked

import com.onedelay.boostcampassignment.data.MovieItem


internal interface LikedMovieContract {

    interface View {

        fun showMovieList(likedList: List<MovieItem>)

        fun updateRemovedList(item: MovieItem)

    }

    interface Presenter {

        fun requestLikeMovieList()

        fun selectDialogMenuOf(item: MovieItem, which: Int)

    }

}