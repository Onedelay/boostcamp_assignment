package com.onedelay.boostcampassignment.data

typealias MovieKey = String


object LikedMovieRepository {

    private val likedMovieMap = HashMap<MovieKey, MovieItem>()

    fun addLikedMovie(item: MovieItem): Boolean {
        return if (!likedMovieMap.containsKey(item.link)) {
            likedMovieMap[item.link] = item
            true
        } else {
            false
        }
    }

    fun removeLikedMovie(item: MovieItem) {
        likedMovieMap.remove(item.link)
    }

    fun getLikedMovieList(): List<MovieItem> = likedMovieMap.values.toList()

}