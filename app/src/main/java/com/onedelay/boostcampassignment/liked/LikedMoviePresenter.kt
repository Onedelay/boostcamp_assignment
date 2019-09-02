package com.onedelay.boostcampassignment.liked

import com.onedelay.boostcampassignment.data.InMemoryDataHolder
import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel
import java.lang.ref.WeakReference
import javax.inject.Inject


internal class LikedMoviePresenter @Inject constructor(
        private val weakView: WeakReference<LikedMovieContract.View>,
        private val inMemoryDataHolder: InMemoryDataHolder

) : LikedMovieContract.Presenter {

    override fun requestLikeMovieList() {
        val list = inMemoryDataHolder.getLikedMovieList().map {
            it.starred = false
            it
        }

        getView()?.showMovieList(list)
    }

    override fun selectDialogMenuOf(item: MovieItemLookFeel, which: Int) {
        when(which) {
            0 -> {
                inMemoryDataHolder.removeLikedMovie(item)

                getView()?.updateRemovedList(item)
            }
        }
    }

    private fun getView(): LikedMovieContract.View? {
        return weakView.get()
    }

}