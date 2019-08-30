package com.onedelay.boostcampassignment.liked

import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel


internal interface LikedMovieContract {
    interface View {
        fun showMovieList(likedList: List<MovieItemLookFeel>)
        fun updateRemovedList(item: MovieItemLookFeel)
    }

    interface Presenter {
        fun requestLikeMovieList()
        fun selectDialogMenuOf(item: MovieItemLookFeel, which: Int)
    }
}