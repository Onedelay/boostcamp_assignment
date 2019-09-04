package com.onedelay.boostcampassignment.network

import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.fly.FlyApi
import io.reactivex.Observable
import javax.inject.Inject


internal class MovieLikePublisher @Inject constructor(
        private val fly: FlyApi

) {

    fun publish(link: String): Observable<Movie> {
        fly.publishAddingLikeMovie(link)

        return Observable.just(fly.fetchMovieList().first { it.link == link })
    }

}