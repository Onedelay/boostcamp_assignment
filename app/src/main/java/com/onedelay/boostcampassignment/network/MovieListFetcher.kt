package com.onedelay.boostcampassignment.network

import com.onedelay.boostcampassignment.data.entity.MovieListEntity
import com.onedelay.boostcampassignment.data.source.RetrofitApi
import io.reactivex.Observable
import javax.inject.Inject


internal class MovieListFetcher @Inject constructor(private val retrofit: RetrofitApi) {

    fun fetch(movieName: String, start: Int): Observable<MovieListEntity> {
        return retrofit.service.requestMovieInfo(movieName, start)
                .map {
                    MovieListEntity(
                            items = it.items.map { movie -> movie.convertToMovie() },
                            total = it.items.size
                    )
                }
                .toObservable()
    }

}