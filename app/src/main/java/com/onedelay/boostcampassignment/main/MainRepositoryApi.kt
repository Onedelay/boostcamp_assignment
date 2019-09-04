package com.onedelay.boostcampassignment.main

import com.onedelay.boostcampassignment.data.dto.MovieList
import io.reactivex.Single


internal interface MainRepositoryApi {
    fun fetchMovieList(query: String, start: Int): Single<MovieList>
}