package com.onedelay.boostcampassignment.main

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import com.bumptech.glide.Glide
import com.onedelay.boostcampassignment.data.MovieItem
import kotlinx.android.synthetic.main.viewholder_item.view.*


class MovieViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    interface ItemClickListener {
        fun onClick(item: MovieItem)
        fun onLongClick(item: MovieItem)
    }

    private var listener: ItemClickListener? = null

    private lateinit var item: MovieItem

    init {

        itemView.apply {
            setOnClickListener {
                listener?.onClick(item)
            }

            setOnLongClickListener {
                listener?.onLongClick(item)
                true
            }
        }

    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    fun bind(item: MovieItem) {
        this.item = item

        // FIXME: 이렇게 작성하면 내부적으로 findViewById 호출하기때문에 비효율적
        with(itemView) {
            Glide.with(iv_thumb.context)
                    .load(item.image)
                    .into(iv_thumb)

            ratingBar.rating = item.userRating.toFloat() / 2F

            tv_title.text = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(item.title, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(item.title)
            }

            tv_date.text = item.pubDate

            tv_director.text = item.director

            tv_actor.text = item.actor
        }

    }

}
