package com.onedelay.boostcampassignment.movie.dto

import com.onedelay.boostcampassignment.movie.custom.MovieLayout


internal sealed class MovieViewAction {
    sealed class Click : MovieViewAction() {
        class Search(val movieName: String) : Click()

        class ActionSearch(val movieName: String) : Click()

        class OptionMenuLike : Click()

        class ItemElement(val movieLink: String) : Click()

        class LikeMovie(val item: MovieLayout.LooknFeel) : Click()

        class RemoveMovie(val item: MovieLayout.LooknFeel) : Click()
    }

    class LoadMore(val start: Int) : MovieViewAction()
}