package com.onedelay.boostcampassignment.movie.source

import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.fly.FlyApi
import com.onedelay.boostcampassignment.network.MovieListFetcher
import io.reactivex.Observable
import javax.inject.Inject


internal class MovieDataSource @Inject constructor(
        private val movieListFetcher: MovieListFetcher,
        private val fly: FlyApi

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
                .map {
                    if(start == 1) {
                        fly.publishMovieList(it)
                    } else {
                        fly.publishAddingMovieList(it)
                    }

                    it
                }
    }

    override fun publishMovieLike(link: String, starred: Boolean): Movie {
        return if (!starred) {
            fly.publishAddingLikeMovie(link)
        } else {
            fly.publishRemovingLikeMovie(link)
        }
    }

    override fun publishMovieDelete(link: String): Movie {
        return fly.publishRemovingMovie(link)
    }

    override fun ofUpdateLikedMovieChannel(): Observable<List<Movie>> {
        return fly.getLikedMovieUpdateChannel()
    }
}