package com.onedelay.boostcampassignment.data.entity


internal class MovieListEntity(
        val items: List<MovieEntity>,
        val total: Int

) {

    class MovieEntity(
            val title: String,
            val link: String,
            val image: String,
            val pubDate: String,
            val director: String,
            val actor: String,
            val userRating: String
    )

}