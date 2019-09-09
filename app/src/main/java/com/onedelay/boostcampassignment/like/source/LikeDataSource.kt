package com.onedelay.boostcampassignment.like.source

import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.fly.FlyApi
import io.reactivex.Observable
import javax.inject.Inject


internal class LikeDataSource @Inject constructor(
        private val fly: FlyApi

) : LikeDataSourceApi {

    override fun getLikedMovieList(): List<Movie> {
        return fly.fetchLikedMovies()
    }

    override fun fetchLikedMovieList(): Observable<List<Movie>> {
        return fly.getLikedMovieUpdateChannel()
    }

    override fun removeLikedMovie(link: String): Movie {
        return fly.publishRemovingLikeMovie(link)
    }

}