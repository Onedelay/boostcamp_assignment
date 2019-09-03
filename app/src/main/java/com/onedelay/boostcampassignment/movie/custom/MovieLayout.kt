package com.onedelay.boostcampassignment.movie.custom

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.onedelay.boostcampassignment.R
import kotlinx.android.synthetic.main.view_movie_item.view.*


internal class MovieLayout constructor(
        context: Context,
        attrs: AttributeSet? = null

) : FrameLayout(context, attrs) {

    class LooknFeel(
            val title: String,
            val link: String,
            val image: String,
            val pubDate: String,
            val director: String,
            val actor: String,
            val userRating: String
    ) {
        var starred: Boolean = false
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_movie_item, this, true)
    }

    fun setLooknFeel(looknFeel: LooknFeel) {
        Glide.with(iv_thumb.context)
                .load(looknFeel.image)
                .into(iv_thumb)

        ratingBar.rating = looknFeel.userRating.toFloat() / 2F

        tv_title.text = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(looknFeel.title, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(looknFeel.title)
        }

        tv_date.text = looknFeel.pubDate

        tv_director.text = looknFeel.director

        tv_actor.text = looknFeel.actor

        iv_starred.visibility = if(looknFeel.starred) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}