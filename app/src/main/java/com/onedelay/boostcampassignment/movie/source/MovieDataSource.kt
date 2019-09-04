package com.onedelay.boostcampassignment.movie.source

import com.onedelay.boostcampassignment.data.dto.Movie
import com.onedelay.boostcampassignment.network.*
import io.reactivex.Observable
import javax.inject.Inject


internal class MovieDataSource @Inject constructor(
        private val movieListFetcher: MovieListFetcher,
        private val movieListPublisher: MovieListPublisher,
        private val movieListAddPublisher: MovieListAddPublisher,
        private val movieLikePublisher: MovieLikePublisher,
        private val movieRemoveLikePublisher: MovieRemoveLikePublisher,
        private val movieDeletePublisher: MovieDeletePublisher

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
                        movieListPublisher.publish(it)
                    } else {
                        movieListAddPublisher.publish(it)
                    }

                    it
                }
    }

    override fun publishMovieLike(link: String, starred: Boolean): Observable<Movie> {
        return if(starred) {
            movieLikePublisher.publish(link)
        } else {
            movieRemoveLikePublisher.publish(link)
        }
    }

    override fun publishMovieDelete(link: String): Observable<Boolean> {
        return movieDeletePublisher.publish(link)
    }

}