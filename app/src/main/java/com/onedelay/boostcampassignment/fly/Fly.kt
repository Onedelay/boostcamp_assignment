package com.onedelay.boostcampassignment.fly

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import com.onedelay.boostcampassignment.data.dto.Movie
import io.reactivex.Observable
import javax.inject.Inject

typealias MovieKey = String


internal class Fly @Inject constructor() : FlyApi {

    private val movieList = mutableListOf<Movie>()

    private val likedMovieMap = HashMap<MovieKey, Movie>()

    override fun fetchMovieList() = movieList

    override fun fetchLikedMovieList() = likedMovieMap.values.toList()

    private val likeMovieChannel: Relay<Movie> = BehaviorRelay.create()

    private val removeLikeMovieChannel: Relay<Movie> = BehaviorRelay.create()

    override fun publishMovieList(movieList: List<Movie>) {
        this.movieList.clear()
        this.movieList.addAll(movieList)
    }

    override fun publishAddingMovieList(movieList: List<Movie>) {
        this.movieList.addAll(movieList)
    }

    override fun publishAddingLikeMovie(link: String): Observable<Movie> {
        val index = this.movieList.indexOfFirst { it.link == link }

        val movie = this.movieList[index].apply {
            starred = true
        }

        likeMovieChannel.accept(movie)

        this.likedMovieMap[link] = movie


        return Observable.just(movie)
    }

    override fun publishRemovingLikeMovie(link: String): Observable<Movie> {
        val index = this.movieList.indexOfFirst { it.link == link }

        val movie = this.movieList[index].apply {
            starred = false
        }

        removeLikeMovieChannel.accept(movie)

        this.likedMovieMap.remove(link)

        return Observable.just(this.movieList[index])
    }

    override fun publishDeletingMovie(link: String): Movie {
        val removedMovie = movieList.first { it.link == link }

        this.movieList.removeIf { it.link == link }

        return removedMovie
    }

    override fun channelOfAddedLikeMovie(): Relay<Movie> = likeMovieChannel

    override fun channelOfRemovedLikeMovie(): Relay<Movie> = removeLikeMovieChannel
}