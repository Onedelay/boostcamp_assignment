package com.onedelay.boostcampassignment.fly

import com.onedelay.boostcampassignment.data.dto.Movie


internal interface FlyApi {
    fun fetchMovieList(): List<Movie>
    fun fetchLikedMovieList(): List<Movie>
    fun publishMovieList(movieList: List<Movie>)
    fun publishAddingMovieList(movieList: List<Movie>)
    fun publishToggleMovie(link: String)
    fun publishAddingLikeMovie(link: String)
    fun publishRemovingLikeMovie(link: String)
    fun publishDeletingMovie(link: String)
}