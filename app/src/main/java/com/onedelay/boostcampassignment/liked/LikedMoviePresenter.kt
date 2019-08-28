package com.onedelay.boostcampassignment.liked

import com.onedelay.boostcampassignment.data.InMemoryDataHolder
import com.onedelay.boostcampassignment.data.MovieItem


internal class LikedMoviePresenter constructor(
        private val view: LikedMovieContract.View,
        private val likedRepository: InMemoryDataHolder

) : LikedMovieContract.Presenter {

    override fun requestLikeMovieList() {
        view.showMovieList(likedRepository.getLikedMovieList())
    }

    override fun selectDialogMenuOf(item: MovieItem, which: Int) {
        when(which) {
            0 -> {
                likedRepository.removeLikedMovie(item)
                view.updateRemovedList(item)
            }
        }
    }

}