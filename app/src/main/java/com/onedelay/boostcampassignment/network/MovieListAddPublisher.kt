package com.onedelay.boostcampassignment.network

import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.fly.FlyApi
import io.reactivex.Observable
import javax.inject.Inject


internal class MovieListAddPublisher @Inject constructor(
        private val fly: FlyApi

) {

    fun publish(movieList: List<Movie>): Observable<List<Movie>> {
        fly.publishAddingMovieList(movieList)

        return Observable.just(movieList)
    }

}