package com.onedelay.boostcampassignment.movie.dto

import com.onedelay.boostcampassignment.data.dto.Movie


internal sealed class MovieDataEvent {
    class MovieListFetched(val movieList: List<Movie>) : MovieDataEvent()

    class MoreMovieListFetched(val movieList: List<Movie>) : MovieDataEvent()

    class MovieItemUpdated(val movieItem: Movie) : MovieDataEvent()

    class MovieItemRemoved(val movieItem: Movie) : MovieDataEvent()
}