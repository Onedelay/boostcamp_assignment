package com.onedelay.boostcampassignment.data.dto

import com.onedelay.boostcampassignment.data.entity.MovieListEntity
import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel


internal class MovieItem(
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

    fun convertToMovie(): MovieListEntity.Movie {
        return MovieListEntity.Movie(
                title      = this.title,
                link       = this.link,
                image      = this.image,
                pubDate    = this.pubDate,
                director   = this.director,
                actor      = this.actor,
                userRating = this.userRating
        )
    }

}