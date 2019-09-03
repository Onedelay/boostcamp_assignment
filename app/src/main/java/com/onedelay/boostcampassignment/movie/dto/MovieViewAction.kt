package com.onedelay.boostcampassignment.movie.dto


internal sealed class MovieViewAction {
    sealed class Click : MovieViewAction() {
        class Search(val movieName: String) : Click()

        class ActionSearch(val movieName: String) : Click()

        class OptionMenuLike : Click()
    }
}