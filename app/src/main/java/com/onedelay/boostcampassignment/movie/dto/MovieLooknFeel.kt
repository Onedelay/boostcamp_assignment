package com.onedelay.boostcampassignment.movie.dto

import com.onedelay.boostcampassignment.movie.custom.MovieLayout


internal sealed class MovieLooknFeel {
    class BindMovieRecyclerView(val movieLooknFeelList: List<MovieLayout.LooknFeel>) : MovieLooknFeel()

    class BindMoreMovieRecyclerView(val movieLooknFeelList: List<MovieLayout.LooknFeel>) : MovieLooknFeel()
}