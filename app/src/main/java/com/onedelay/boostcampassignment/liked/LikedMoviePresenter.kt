package com.onedelay.boostcampassignment.liked

import com.onedelay.boostcampassignment.data.InMemoryDataHolder
import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel


internal class LikedMoviePresenter constructor(
        private val view: LikedMovieContract.View,
        private val likedRepository: InMemoryDataHolder

) : LikedMovieContract.Presenter {

    override fun requestLikeMovieList() {
        val list = likedRepository.getLikedMovieList().map {
            it.starred = false
            it
        }
        view.showMovieList(list)
    }

    override fun selectDialogMenuOf(item: MovieItemLookFeel, which: Int) {
        when(which) {
            0 -> {
                likedRepository.removeLikedMovie(item)
                view.updateRemovedList(item)
            }
        }
    }

}