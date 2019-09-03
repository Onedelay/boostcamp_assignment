package com.onedelay.boostcampassignment.movie.source

import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.network.MovieListFetcher
import io.reactivex.Observable
import javax.inject.Inject


internal class MovieDataSource @Inject constructor(
        private val movieListFetcher: MovieListFetcher

) : MovieDataSourceApi {

    override fun fetchMovies(movieName: String, start: Int): Observable<List<Movie>> {
        return movieListFetcher.fetch(movieName, start)
                .map {
                    it.items.map { movie ->
                        Movie(
                           title      = movie.title,
                           link       = movie.link,
                           image      = movie.image,
                           pubDate    = movie.pubDate,
                           director   = movie.director,
                           actor      = movie.actor,
                           userRating = movie.userRating
                        )
                    }
                }
    }

}