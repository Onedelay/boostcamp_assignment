package com.onedelay.boostcampassignment.like.source

import com.onedelay.boostcampassignment.data.dto.Movie
import io.reactivex.Observable


internal interface LikeDataSourceApi {
    fun getLikedMovieList(): List<Movie>
    fun fetchLikedMovieList(): Observable<List<Movie>>
    fun removeLikedMovie(link: String): Movie
}