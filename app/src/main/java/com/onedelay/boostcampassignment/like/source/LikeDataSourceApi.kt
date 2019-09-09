package com.onedelay.boostcampassignment.like.source

import com.onedelay.boostcampassignment.data.dto.Movie
import io.reactivex.Observable


internal interface LikeDataSourceApi {
    fun fetchLikedMovieList(): List<Movie>
    fun removeLikedMovie(link: String): Movie
    fun ofUpdateLikedMovieChannel(): Observable<List<Movie>>
}