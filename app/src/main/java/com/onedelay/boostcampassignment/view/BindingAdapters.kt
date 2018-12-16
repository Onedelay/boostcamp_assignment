package com.onedelay.boostcampassignment.view

import android.databinding.BindingAdapter
import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class BindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("android:html")
        fun setHtml(textView: TextView, html: String) {
            textView.text =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
                    else Html.fromHtml(html)
        }

        @JvmStatic
        @BindingAdapter("android:image")
        fun setImage(imageView: ImageView, url: String) {
            Glide.with(imageView.context)
                    .load(url)
                    .into(imageView)
        }
    }
}