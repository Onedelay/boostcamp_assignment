package com.onedelay.boostcampassignment.movie.repository

import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.extension.pairWithLatestFrom
import com.onedelay.boostcampassignment.movie.MovieViewModel
import com.onedelay.boostcampassignment.movie.dto.MovieDataEvent
import com.onedelay.boostcampassignment.movie.source.MovieDataSourceApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


internal class MovieRepository @Inject constructor(
        private val movieDataSource: MovieDataSourceApi

) : MovieRepositoryApi {

    override fun setViewModel(viewModel: MovieViewModel) {
        with(viewModel) {
            val fetchMovies = Observable.merge(
                    viewActionInput.clickSearch.map { it.movieName },
                    viewActionInput.clickActionSearch.map { it.movieName }
            )

            val fetchMoviesCall = fetchMovies
                    .observeOn(Schedulers.io())
                    .switchMap { movieDataSource.fetchMovies(movieName = it, start = 1) }

            val updateMovieLike = movieDataSource.ofUpdateLikedMovieChannel()

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

            val fetchMoreMovies = viewActionInput.loadMoreMovieList
                    .pairWithLatestFrom(fetchMovies)
                    .observeOn(Schedulers.io())
                    .switchMap {
                        movieDataSource.fetchMovies(movieName = it.second, start = it.first.start)
                    }

            val publishMovieLike = viewActionInput.clickMovieLike
                    .observeOn(Schedulers.io())
                    .map {
                        movieDataSource.publishMovieLike(link = it.item.link, starred = it.item.starred)
                    }

            val publishMovieRemove = viewActionInput.clickMovieRemove
                    .observeOn(Schedulers.io())
                    .map {
                        movieDataSource.publishMovieDelete(link = it.item.link)
                    }

            disposable.addAll(
                    fetchMoviesCall
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { channel.accept(MovieDataEvent.MovieListFetched(it)) },

                    fetchMoreMovies
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { channel.accept(MovieDataEvent.MoreMovieListFetched(it)) },

                    fetchLikedMovieUpdate
                            .subscribe { channel.accept(MovieDataEvent.LikedMovieItemList(it)) },

                    publishMovieLike
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { },

                    publishMovieRemove
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { channel.accept(MovieDataEvent.MovieItemRemoved(it)) }
            )
        }
    }

}