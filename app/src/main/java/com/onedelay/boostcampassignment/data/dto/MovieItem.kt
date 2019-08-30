package com.onedelay.boostcampassignment.data.dto

import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel


class MovieItem(
        private val title: String,
        private val link: String,
        private val image: String,
        private val pubDate: String,
        private val director: String,
        private val actor: String,
        private val userRating: String

) {

    fun convertToLooknFeel(): MovieItemLookFeel {
        return MovieItemLookFeel(title, link, image, pubDate, director, actor, userRating)
    }

}