package com.onedelay.boostcampassignment.movie.repository

import android.util.Log
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
                    .observeOn(Schedulers.io())
                    .switchMap { movieDataSource.fetchMovies(it, 1) } //FIXME

            disposable.addAll(
                    fetchMovies
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError { Log.d("MovieRepository", "${it.printStackTrace()}") }
                            .subscribe { channel.accept(MovieDataEvent.MovieListFetched(it)) }
            )
        }
    }

}