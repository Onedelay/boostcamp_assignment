package com.onedelay.boostcampassignment.movie.source

import com.onedelay.boostcampassignment.data.dto.Movie
import io.reactivex.Observable


internal interface MovieDataSourceApi {
    fun fetchMovies(movieName: String, start: Int): Observable<List<Movie>>
}