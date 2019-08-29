package com.onedelay.boostcampassignment.data.dto

import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel


class MovieItem(
        val title: String,
        val link: String,
        val image: String,
        val pubDate: String,
        val director: String,
        val actor: String,
        val userRating: String
) {

    fun convertToLooknFeel(): MovieItemLookFeel {
        return MovieItemLookFeel(title, link, image, pubDate, director, actor, userRating)
    }

}