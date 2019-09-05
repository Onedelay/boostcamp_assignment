package com.onedelay.boostcampassignment.like.dto

import com.onedelay.boostcampassignment.movie.custom.MovieLayout


internal sealed class LikeViewAction {
    sealed class Click : LikeViewAction() {
        class ItemElement(val movieLink: String) : Click()

        class RemoveMovie(val item: MovieLayout.LooknFeel) : Click()
    }
}