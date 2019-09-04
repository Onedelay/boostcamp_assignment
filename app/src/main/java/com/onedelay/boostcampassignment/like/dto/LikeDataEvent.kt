package com.onedelay.boostcampassignment.like.dto

import com.onedelay.boostcampassignment.data.dto.Movie


internal sealed class LikeDataEvent {

    class LikedMovieListFetched(val likedMovieList: List<Movie>) : LikeDataEvent()

}