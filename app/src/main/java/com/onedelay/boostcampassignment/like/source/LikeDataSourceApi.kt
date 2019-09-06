package com.onedelay.boostcampassignment.like.source

import com.onedelay.boostcampassignment.data.dto.Movie
import io.reactivex.Observable


internal interface LikeDataSourceApi {
    fun fetchLikedMovies(): Observable<List<Movie>>
    fun removeLikedMovie(link: String): Observable<Movie>
}