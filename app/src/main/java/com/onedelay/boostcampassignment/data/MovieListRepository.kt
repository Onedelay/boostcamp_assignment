package com.onedelay.boostcampassignment.data

import com.onedelay.boostcampassignment.data.dto.MovieList
import com.onedelay.boostcampassignment.data.source.RetrofitApi
import io.reactivex.Single
import javax.inject.Inject


internal class MovieListRepository @Inject constructor(
        private val movieRemoteDataSource: RetrofitApi

) : MainRepositoryApi {

    override fun fetchMovieList(query: String, start: Int): Single<MovieList> {
        return movieRemoteDataSource.service.requestMovieInfo(query, start)
    }

}