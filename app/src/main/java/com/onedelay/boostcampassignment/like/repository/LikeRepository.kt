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
            val initializeLikedMovies = lifecycleInput.onCreate.map { LikeDataEvent.LikedMovieListFetched(dataSource.getLikedMovieList()) }

            val fetchLikedMovies = dataSource.fetchLikedMovieList().map { LikeDataEvent.LikedMovieItemList(it) }

            val removeLikedMovies = viewActionInput.clickMovieRemove.map { dataSource.removeLikedMovie(link = it.item.link) }

            disposable.addAll(
                    initializeLikedMovies.subscribe { channel.accept(it) },

                    fetchLikedMovies.subscribe { channel.accept(it) },

                    removeLikedMovies.subscribe { }
            )
        }
    }

}