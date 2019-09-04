package com.onedelay.boostcampassignment.like.dto

import com.onedelay.boostcampassignment.movie.custom.MovieLayout


internal sealed class LikeLooknFeel {
    class BindMovieRecyclerView(val movieLooknFeelList: List<MovieLayout.LooknFeel>) : LikeLooknFeel()
}