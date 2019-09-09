package com.onedelay.boostcampassignment.data.dto


internal class Movie(
        val title: String,
        val link: String,
        val image: String,
        val pubDate: String,
        val director: String,
        val actor: String,
        val userRating: String
) {
    var starred = false

    override fun equals(other: Any?): Boolean {
        if (other is Movie) {
            return this.link == other.link
        }
        return super.equals(other)
    }
}