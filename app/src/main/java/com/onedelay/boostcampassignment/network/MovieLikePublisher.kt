package com.onedelay.boostcampassignment.network

import com.jakewharton.rxrelay2.Relay
import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.fly.FlyApi
import io.reactivex.Observable
import javax.inject.Inject


internal class MovieLikePublisher @Inject constructor(
        private val fly: FlyApi

) {

    fun publish(link: String): Observable<Movie> {
        return fly.publishAddingLikeMovie(link)
    }

    ////////////////////////////

    fun ofChannel(): Relay<Movie> = fly.channelOfAddedLikeMovie()

}