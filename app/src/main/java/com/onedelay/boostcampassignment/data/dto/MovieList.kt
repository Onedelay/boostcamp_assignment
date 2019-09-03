package com.onedelay.boostcampassignment.data.dto

import com.onedelay.boostcampassignment.data.looknfeel.MovieItemLookFeel


internal class MovieList(val items: List<MovieItem>, val total: Int) {

    fun convertToLookFeel(): List<MovieItemLookFeel> {
        return items.map { it.convertToLooknFeel() }
    }

}