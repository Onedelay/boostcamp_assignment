package com.onedelay.boostcampassignment.fly

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.onedelay.boostcampassignment.data.dto.Movie
import io.reactivex.Observable
import javax.inject.Inject


typealias MovieKey = String

internal class Fly @Inject constructor() : FlyApi {

    private val movieList = mutableListOf<Movie>()

    private val likedMovieMap = LinkedHashMap<MovieKey, Movie>()

    private val likedMovieUpdateChannel: Relay<List<Movie>> = PublishRelay.create()

    override fun fetchMovieList() = movieList

    override fun fetchLikedMovies(): List<Movie> {
        return this.likedMovieMap.values.toList()
    }

    override fun publishMovieList(movieList: List<Movie>) {
        this.movieList.clear()
        this.movieList.addAll(movieList)
    }

    override fun publishAddingMovieList(movieList: List<Movie>) {
        this.movieList.addAll(movieList)
    }

    override fun publishRemovingMovie(link: String): Movie {
        val removedMovie = movieList.first { it.link == link }

        this.movieList.removeIf { it.link == link }

        return removedMovie
    }

    override fun publishAddingLikeMovie(link: String): Movie {
        val index = this.movieList.indexOfFirst { it.link == link }

        val movie = this.movieList[index].apply {
            starred = true
        }

        this.likedMovieMap[link] = movie

        likedMovieUpdateChannel.accept(likedMovieMap.values.toList())

        return movie
    }

    override fun publishRemovingLikeMovie(link: String): Movie {
        val index = this.movieList.indexOfFirst { it.link == link }

        val movie = this.movieList[index].apply {
            starred = false
        }

        this.likedMovieMap.remove(link)

        likedMovieUpdateChannel.accept(likedMovieMap.values.toList())

        return movie
    }

    override fun getLikedMovieUpdateChannel(): Observable<List<Movie>> {
        return this.likedMovieUpdateChannel
    }

}