package com.onedelay.boostcampassignment.fly

import com.onedelay.boostcampassignment.data.dto.Movie
import io.reactivex.Observable


internal interface FlyApi {
    fun fetchMovieList(): List<Movie>
    fun fetchLikedMovies(): List<Movie>
    fun publishMovieList(movieList: List<Movie>)
    fun publishAddingMovieList(movieList: List<Movie>)
    fun publishRemovingMovie(link: String): Movie
    fun publishAddingLikeMovie(link: String): Movie
    fun publishRemovingLikeMovie(link: String): Movie
    fun getLikedMovieUpdateChannel(): Observable<List<Movie>>
}