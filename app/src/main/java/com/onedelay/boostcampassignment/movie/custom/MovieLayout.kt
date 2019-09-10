package com.onedelay.boostcampassignment.movie.custom

import android.content.Context
import android.os.Build
import android.os.Parcelable
import android.text.Html
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.onedelay.boostcampassignment.R
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.view_movie_item.view.*


internal class MovieLayout constructor(
        context: Context,
        attrs: AttributeSet? = null

) : FrameLayout(context, attrs) {

    @Parcelize
    class LooknFeel(
            val title: String,
            val link: String,
            val image: String,
            val pubDate: String,
            val director: String,
            val actor: String,
            val userRating: String,
            var starred: Boolean = false,
            var isBigImageShowing: Boolean = false
    ) : Parcelable {
        override fun equals(other: Any?): Boolean {
            if (other is LooknFeel) {
                return this.link == other.link
            }
            return super.equals(other)
        }

        override fun hashCode(): Int {
            return link.hashCode()
        }
    }

    private var looknFeel: LooknFeel? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_movie_item, this, true)

        initializeListener()
    }

    fun setLooknFeel(looknFeel: LooknFeel) {
        this.looknFeel = looknFeel

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

        if(looknFeel.isBigImageShowing) {
            showBigImage(looknFeel.image)
        } else {
            hideBigImage()
        }
    }

    fun updateLikedState(looknFeel: LooknFeel) {
        iv_starred.visibility = if(looknFeel.starred) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun initializeListener() {
        iv_thumb.setOnClickListener {
            showBigImage(looknFeel!!.image)
        }

        iv_movie_big.setOnClickListener {
            hideBigImage()
        }
    }

    private fun showBigImage(image: String) {
        iv_movie_big.visibility = View.VISIBLE

        Glide.with(iv_movie_big.context)
                .load(image)
                .into(iv_movie_big)

        looknFeel!!.isBigImageShowing = true
    }

    private fun hideBigImage() {
        iv_movie_big.visibility = View.GONE

        looknFeel!!.isBigImageShowing = false
    }

}