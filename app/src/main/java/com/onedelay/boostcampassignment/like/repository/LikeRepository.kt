package com.onedelay.boostcampassignment.like.repository

import com.onedelay.boostcampassignment.like.LikeViewModel
import com.onedelay.boostcampassignment.like.dto.LikeDataEvent
import com.onedelay.boostcampassignment.like.source.LikeDataSourceApi
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import javax.inject.Inject


internal class LikeRepository @Inject constructor(
        private val dataSource: LikeDataSourceApi

) : LikeRepositoryApi {

    override fun setViewModel(viewModel: LikeViewModel) {
        with(viewModel) {
            val fetchLikedMovies = Observable.merge(
                    lifecycleInput.onCreate
                            .map { LikeDataEvent.LikedMovieListFetched(dataSource.getLikedMovieList()) },
                    Observables.combineLatest(
                            lifecycleInput.onCreate,
                            dataSource.fetchLikedMovieList()
                    )
                            .map { LikeDataEvent.LikedMovieItemList(it.second) }
                            .skip(1)
            )

            val removeLikedMovies = viewActionInput.clickMovieRemove
                    .map {
                        dataSource.removeLikedMovie(link = it.item.link)
                    }

            disposable.addAll(
                    fetchLikedMovies
                            .subscribe { channel.accept(it) },

                    removeLikedMovies
                            .subscribe { }
            )
        }
    }

}