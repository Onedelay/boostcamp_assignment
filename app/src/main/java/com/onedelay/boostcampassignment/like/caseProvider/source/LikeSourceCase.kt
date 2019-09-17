package com.onedelay.boostcampassignment.like.caseProvider.source

import com.onedelay.boostcampassignment.like.caseProvider.LikeCaseProvider
import com.onedelay.boostcampassignment.like.dto.LikeDataEvent
import com.onedelay.boostcampassignment.like.source.LikeDataSourceApi
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject


@Suppress("HasPlatformType")
internal class LikeSourceCase @Inject constructor(
    private val caseProvider: LikeCaseProvider,
    private val dataSource: LikeDataSourceApi

) : LikeSourceCaseApi {

    private val fetch by lazy(LazyThreadSafetyMode.NONE) { Fetch() }

    private val publish by lazy(LazyThreadSafetyMode.NONE) { Publish() }

    inner class Fetch {
        val initializeLikedMovies = caseProvider.LifeCycleInput().onCreate
                .filter { it.savedInstanceState == null }
                .map {
                    LikeDataEvent.LikedMovieListFetched(dataSource.getLikedMovieList())
                }

        val fetchLikedMovies = dataSource.fetchLikedMovieList()
                .map {
                    LikeDataEvent.LikedMovieItemList(it)
                }
    }

    inner class Publish {
        val removeLikedMovie = caseProvider.ViewActionInput().clickMovieLike
                .map {
                    dataSource.removeLikedMovie(link = it.item.link)
                }
    }

    init {
        caseProvider.disposable().addAll(
                fetch.initializeLikedMovies
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { caseProvider.channel().accept(LikeDataEvent.LikedMovieListFetched(it.likedMovieList)) },

                fetch.fetchLikedMovies
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { caseProvider.channel().accept(LikeDataEvent.LikedMovieItemList(it.likedMovieList)) },

                publish.removeLikedMovie
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { }
        )
    }

}