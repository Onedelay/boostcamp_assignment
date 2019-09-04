package com.onedelay.boostcampassignment.main

import com.onedelay.boostcampassignment.data.dto.MovieItem
import com.onedelay.boostcampassignment.data.dto.MovieList
import com.onedelay.boostcampassignment.data.source.RetrofitApi
import io.reactivex.Single
import javax.inject.Inject


internal class MainRepository @Inject constructor(
        private val movieRemoteDataSource: RetrofitApi

) : MainRepositoryApi {

    override fun fetchMovieList(query: String, start: Int): Single<MovieList> {
        return movieRemoteDataSource.service.requestMovieInfo(query, start)
                .map {
                    MovieList(
                            items = it.items.map { item ->
                                MovieItem(
                                        item.title,
                                        item.link,
                                        item.image,
                                        item.pubDate,
                                        item.director,
                                        item.actor,
                                        item.userRating
                                )
                            },
                            total = it.total
                    )
                }
    }

}