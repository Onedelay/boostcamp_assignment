package com.onedelay.boostcampassignment.like.dto


internal sealed class LikeNavigation {
    class ToWebViewActivity(val movieLink: String) : LikeNavigation()
}