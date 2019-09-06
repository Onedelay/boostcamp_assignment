package com.onedelay.boostcampassignment.fly

import com.jakewharton.rxrelay2.Relay
import com.onedelay.boostcampassignment.data.dto.Movie
import io.reactivex.Observable


internal interface FlyApi {
    fun fetchMovieList(): List<Movie>
    fun fetchLikedMovieList(): List<Movie>
    fun publishMovieList(movieList: List<Movie>)
    fun publishAddingMovieList(movieList: List<Movie>)
    fun publishAddingLikeMovie(link: String): Observable<Movie>
    fun publishRemovingLikeMovie(link: String): Observable<Movie>
    fun publishDeletingMovie(link: String): Movie
    fun channelOfAddedLikeMovie(): Relay<Movie>
    fun channelOfRemovedLikeMovie(): Relay<Movie>
}