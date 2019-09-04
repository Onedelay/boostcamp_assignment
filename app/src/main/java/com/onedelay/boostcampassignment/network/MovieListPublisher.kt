package com.onedelay.boostcampassignment.network

import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.fly.FlyApi
import io.reactivex.Observable
import javax.inject.Inject


internal class MovieListPublisher @Inject constructor(
        private val fly: FlyApi

) {

    fun publish(movieList: List<Movie>): Observable<List<Movie>> {
        fly.publishMovieList(movieList)

        return Observable.just(movieList)
    }

}