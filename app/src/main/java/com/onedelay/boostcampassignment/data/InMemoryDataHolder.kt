package com.onedelay.boostcampassignment.data

import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel

typealias MovieKey = String


object InMemoryDataHolder {

    private val likedMovieMap = HashMap<MovieKey, MovieItemLookFeel>()

    fun addLikedMovie(item: MovieItemLookFeel): Boolean {
        return if (!likedMovieMap.containsKey(item.link)) {
            likedMovieMap[item.link] = item
            true
        } else {
            false
        }
    }

    fun removeLikedMovie(item: MovieItemLookFeel) {
        likedMovieMap.remove(item.link)
    }

    fun getLikedMovieMap(): Map<MovieKey, MovieItemLookFeel> = likedMovieMap

    fun getLikedMovieList(): List<MovieItemLookFeel> = likedMovieMap.values.toList()

}