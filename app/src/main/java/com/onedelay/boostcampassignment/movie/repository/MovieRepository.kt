package com.onedelay.boostcampassignment.movie.repository

import com.onedelay.boostcampassignment.extension.pairWithLatestFrom
import com.onedelay.boostcampassignment.movie.MovieViewModel
import com.onedelay.boostcampassignment.movie.dto.MovieDataEvent
import com.onedelay.boostcampassignment.movie.source.MovieDataSourceApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


internal class MovieRepository @Inject constructor(private val movieDataSource: MovieDataSourceApi) : MovieRepositoryApi {

    override fun setViewModel(viewModel: MovieViewModel) {
        with(viewModel) {
            val fetchMovies = Observable.merge(
                    viewActionInput.clickSearch.map { it.movieName },
                    viewActionInput.clickActionSearch.map { it.movieName }
            )

            val fetchMoviesCall = fetchMovies
                    .observeOn(Schedulers.io())
                    .switchMap { movieDataSource.fetchMovies(movieName = it, start = 1) }

            val fetchMoreMovies = viewActionInput.loadMoreMovieList
                    .pairWithLatestFrom(fetchMovies)
                    .observeOn(Schedulers.io())
                    .switchMap {
                        movieDataSource.fetchMovies(movieName = it.second, start = it.first.start)
                    }

            val publishMovieLike = viewActionInput.clickMovieLike
                    .observeOn(Schedulers.io())
                    .switchMap {
                        movieDataSource.publishMovieLike(link = it.item.link, starred = it.item.starred)
                    }

            val publishMovieRemove = viewActionInput.clickMovieRemove
                    .observeOn(Schedulers.io())
                    .switchMap {
                        movieDataSource.publishMovieDelete(link = it.item.link)
                    }

            val observeMovieUpdate = Observable.merge(
                    movieDataSource.observeLikeMovieChannel(),
                    movieDataSource.observeRemoveLikeMovieChannel()
            )

            disposable.addAll(
                    fetchMoviesCall
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { channel.accept(MovieDataEvent.MovieListFetched(it)) },

                    fetchMoreMovies
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { channel.accept(MovieDataEvent.MoreMovieListFetched(it)) },

                    publishMovieLike
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { },

                    publishMovieRemove
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { channel.accept(MovieDataEvent.MovieItemRemoved(it)) },

                    observeMovieUpdate
                            .subscribe { channel.accept(MovieDataEvent.MovieItemUpdated(it)) }
            )
        }
    }

}