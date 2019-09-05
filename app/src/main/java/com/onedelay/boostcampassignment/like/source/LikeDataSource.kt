package com.onedelay.boostcampassignment.like.source

import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.fly.FlyApi
import io.reactivex.Observable
import javax.inject.Inject


internal class LikeDataSource @Inject constructor(
        private val fly: FlyApi
) : LikeDataSourceApi {

    override fun fetchLikedMovies(): Observable<List<Movie>> {
        return Observable.just(
                fly.fetchLikedMovieList()
        )
    }

    override fun removeLikedMovie(link: String): Observable<Movie> {
        return fly.publishRemovingLikeMovie(link)
    }

}