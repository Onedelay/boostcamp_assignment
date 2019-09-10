package com.onedelay.boostcampassignment.movie.caseProvider.source

import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.extension.pairWithLatestFrom
import com.onedelay.boostcampassignment.movie.caseProvider.MovieCaseProviderApi
import com.onedelay.boostcampassignment.movie.dto.MovieDataEvent
import com.onedelay.boostcampassignment.movie.source.MovieDataSourceApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@Suppress("HasPlatformType")
internal class MovieSourceCase @Inject constructor(
        private val caseProvider: MovieCaseProviderApi,
        private val dataSource: MovieDataSourceApi

) : MovieSourceCaseApi {

    private val fetch by lazy(LazyThreadSafetyMode.NONE, ::Fetch)

    private val publish by lazy(LazyThreadSafetyMode.NONE, ::Publish)

    inner class Fetch {
        val fetchMovies = Observable.merge(
                caseProvider.viewAction().clickSearch.map { it.movieName },
                caseProvider.viewAction().clickActionSearch.map { it.movieName }
        )

        val fetchMoviesCall = fetchMovies
                .observeOn(Schedulers.io())
                .switchMap { dataSource.fetchMovies(movieName = it, start = 1) }

        val updateMovieLike = dataSource.ofUpdateLikedMovieChannel()

        val fetchLikedMovieUpdate = Observable.merge(
                updateMovieLike.take(1),
                Observables.zip(updateMovieLike, updateMovieLike.skip(1))
                        .map {
                            val previous = it.first
                            val current  = it.second

                            val largerList = mutableListOf<Movie>()
                            val smallerList = mutableListOf<Movie>()

                            if (previous.size < current.size) {
                                smallerList.addAll(previous)
                                largerList.addAll(current)
                            } else {
                                smallerList.addAll(current)
                                largerList.addAll(previous)
                            }

                            val diffList = mutableListOf<Movie>()
                            for (i in 0 until largerList.size) {
                                if (!smallerList.contains(largerList[i])) {
                                    diffList.add(largerList[i])
                                }
                            }

                            diffList
                        }
        )

        val fetchMoreMovies = caseProvider.viewAction().loadMoreMovieList
                .pairWithLatestFrom(fetchMovies)
                .observeOn(Schedulers.io())
                .switchMap {
                    dataSource.fetchMovies(movieName = it.second, start = it.first.start)
                }
    }

    inner class Publish {
        val publishMovieLike = caseProvider.viewAction().clickMovieLike
                .observeOn(Schedulers.io())
                .map {
                    dataSource.publishMovieLike(link = it.item.link, starred = it.item.starred)
                }

        val publishMovieRemove = caseProvider.viewAction().clickMovieRemove
                .observeOn(Schedulers.io())
                .map {
                    dataSource.publishMovieDelete(link = it.item.link)
                }
    }

    init {
        caseProvider.disposable().addAll(
                fetch.fetchMoviesCall
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { caseProvider.channel().accept(MovieDataEvent.MovieListFetched(it)) },

                fetch.fetchMoreMovies
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { caseProvider.channel().accept(MovieDataEvent.MoreMovieListFetched(it)) },

                fetch.fetchLikedMovieUpdate
                        .subscribe { caseProvider.channel().accept(MovieDataEvent.LikedMovieItemList(it)) },

                publish.publishMovieLike
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { },

                publish.publishMovieRemove
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { caseProvider.channel().accept(MovieDataEvent.MovieItemRemoved(it)) }
        )
    }

}