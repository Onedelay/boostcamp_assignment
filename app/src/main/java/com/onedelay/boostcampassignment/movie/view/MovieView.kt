package com.onedelay.boostcampassignment.movie.view

import com.onedelay.boostcampassignment.movie.dto.MovieLooknFeel
import com.onedelay.boostcampassignment.movie.dto.MovieNavigation


internal interface MovieView {
    fun bindLooknFeel(looknFeel: MovieLooknFeel)
    fun bindNavigation(navigation: MovieNavigation)
}