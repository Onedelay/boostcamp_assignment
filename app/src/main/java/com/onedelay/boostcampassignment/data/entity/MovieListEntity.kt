package com.onedelay.boostcampassignment.data.entity


internal class MovieListEntity(
        val items: List<Movie>,
        val total: Int

) {

    class Movie(
            val title: String,
            val link: String,
            val image: String,
            val pubDate: String,
            val director: String,
            val actor: String,
            val userRating: String
    )

}