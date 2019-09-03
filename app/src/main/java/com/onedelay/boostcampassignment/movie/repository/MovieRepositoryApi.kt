package com.onedelay.boostcampassignment.movie.repository

import com.onedelay.boostcampassignment.movie.MovieViewModel


internal interface MovieRepositoryApi {
    fun setViewModel(viewModel: MovieViewModel)
}