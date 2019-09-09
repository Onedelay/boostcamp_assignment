package com.onedelay.boostcampassignment.like.source

import com.onedelay.boostcampassignment.data.dto.Movie


internal interface LikeDataSourceApi {
    fun fetchLikedMovieList(): List<Movie>
    fun removeLikedMovie(link: String): Movie
}