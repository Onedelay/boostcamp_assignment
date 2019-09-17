package com.onedelay.boostcampassignment.like.view

import com.onedelay.boostcampassignment.like.dto.LikeLooknFeel
import com.onedelay.boostcampassignment.like.dto.LikeNavigation


internal interface LikeView {
    fun bindLooknFeel(looknFeel: LikeLooknFeel)
    fun bindNavigation(navigation: LikeNavigation)
}