package com.onedelay.boostcampassignment.network

import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.fly.FlyApi
import io.reactivex.Observable
import javax.inject.Inject


internal class MovieDeletePublisher @Inject constructor(
        private val fly: FlyApi

) {

    fun publish(link: String): Observable<Movie> {
        return Observable.just(fly.publishRemovingMovie(link))
    }

}