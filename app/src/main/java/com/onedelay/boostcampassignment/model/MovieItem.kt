package com.onedelay.boostcampassignment.model

data class MovieList(val items: List<MovieItem>,
                     val total: Int)

data class MovieItem(val title: String,
                     val link: String,
                     val image: String,
                     val pubDate: String,
                     val director: String,
                     val actor: String,
                     val userRating: String)