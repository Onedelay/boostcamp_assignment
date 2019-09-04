package com.onedelay.boostcampassignment.movie.dto


internal sealed class MovieNavigation {
    class ToLikeActivity : MovieNavigation()

    class ToWebViewActivity(val movieLink: String) : MovieNavigation()
}