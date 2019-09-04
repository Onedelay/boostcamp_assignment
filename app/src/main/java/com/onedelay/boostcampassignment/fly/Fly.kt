package com.onedelay.boostcampassignment.fly

import com.onedelay.boostcampassignment.data.dto.Movie
import javax.inject.Inject

typealias MovieKey = String


internal class Fly @Inject constructor() : FlyApi {

    private val movieList = mutableListOf<Movie>()

    private val likedMovieMap = HashMap<MovieKey, Movie>()

    override fun fetchMovieList() = movieList

    override fun fetchLikedMovieList() = likedMovieMap.values.toList()

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

    override fun publishAddingLikeMovie(link: String) {
        this.movieList
                .filter { it.link == link }
                .forEach {
                    it.starred = true

                    likedMovieMap[link] = it
                }
    }

    override fun publishRemovingLikeMovie(link: String) {
        this.movieList
                .filter { it.link == link }
                .forEach {
                    it.starred = false

                    likedMovieMap.remove(link)
                }
    }

    override fun publishDeletingMovie(link: String) {
        this.movieList.removeIf { it.link == link }
    }

}