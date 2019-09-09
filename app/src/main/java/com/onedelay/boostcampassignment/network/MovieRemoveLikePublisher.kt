package com.onedelay.boostcampassignment.network

import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.fly.FlyApi
import javax.inject.Inject


internal class MovieRemoveLikePublisher @Inject constructor(
        private val fly: FlyApi

) {

    fun publish(link: String): Movie {
        return fly.publishRemovingLikeMovie(link)
    }

}