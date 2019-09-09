package com.onedelay.boostcampassignment.like.repository

import com.onedelay.boostcampassignment.like.LikeViewModel
import com.onedelay.boostcampassignment.like.dto.LikeDataEvent
import com.onedelay.boostcampassignment.like.source.LikeDataSourceApi
import javax.inject.Inject


internal class LikeRepository @Inject constructor(
        private val dataSource: LikeDataSourceApi

) : LikeRepositoryApi {

    override fun setViewModel(viewModel: LikeViewModel) {
        with(viewModel) {
            val fetchLikedMovies = lifecycleInput.onCreate
                    .switchMap {
                        dataSource.ofUpdateLikedMovieChannel()
                    }

            val removeLikedMovies = viewActionInput.clickMovieRemove
                    .map {
                        dataSource.removeLikedMovie(link = it.item.link)
                    }

            disposable.addAll(
                    fetchLikedMovies
                            .subscribe { channel.accept(LikeDataEvent.LikedMovieListFetched(it)) },

                    removeLikedMovies
                            .subscribe { }
            )
        }
    }

}