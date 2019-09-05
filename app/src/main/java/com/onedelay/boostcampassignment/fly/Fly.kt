package com.onedelay.boostcampassignment.fly

import com.jakewharton.rxrelay2.BehaviorRelay
import com.onedelay.boostcampassignment.data.dto.Movie
import io.reactivex.Observable
import javax.inject.Inject

typealias MovieKey = String


internal class Fly @Inject constructor() : FlyApi {

    private val movieList = mutableListOf<Movie>()

    private val likedMovieMap = HashMap<MovieKey, Movie>()

    override fun fetchMovieList() = movieList

    override fun fetchLikedMovieList() = likedMovieMap.values.toList()

    private val likeMovieChannel = BehaviorRelay.create<Movie>()

    private val removeLikeMovieChannel = BehaviorRelay.create<Movie>()

    override fun publishMovieList(movieList: List<Movie>) {
        this.movieList.clear()
        this.movieList.addAll(movieList)
    }

    override fun publishAddingMovieList(movieList: List<Movie>) {
        this.movieList.addAll(movieList)
    }

    override fun publishToggleMovie(link: String) {
        this.movieList
                .filter { it.link == link }
                .forEach {
                    it.starred = !it.starred

                    if(!it.starred) {
                        likedMovieMap[link] = it
                    } else {
                        likedMovieMap.remove(link)
                    }
                }
    }

    override fun publishAddingLikeMovie(link: String): Observable<Movie> {
        this.movieList
                .filter { it.link == link }
                .forEach {
                    it.starred = true

                    likedMovieMap[link] = it
                }

        val movie = movieList.first { it.link == link}

        likeMovieChannel.accept(movie)

        return likeMovieChannel
    }

    override fun publishRemovingLikeMovie(link: String): Observable<Movie> {
        val index = this.movieList.indexOfFirst { it.link == link }

        this.movieList[index].starred = false

        this.likedMovieMap.remove(link)

        removeLikeMovieChannel.accept(this.movieList[index])

        return removeLikeMovieChannel
    }

    override fun publishDeletingMovie(link: String): Movie {
        val removedMovie = movieList.first { it.link == link }

        this.movieList.removeIf { it.link == link }

        return removedMovie
    }

}