package com.onedelay.boostcampassignment.data

import com.onedelay.boostcampassignment.data.dto.MovieList
import com.onedelay.boostcampassignment.data.source.RetrofitApi
import io.reactivex.Single


internal class MovieListRepository constructor(private val movieRemoteDataSource: RetrofitApi) {

    fun fetchMovieList(query: String, start: Int): Single<MovieList> {
        return movieRemoteDataSource.service.requestMovieInfo(query, start)
    }

}