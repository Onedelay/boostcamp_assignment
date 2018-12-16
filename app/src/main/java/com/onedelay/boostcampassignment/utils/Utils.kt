package com.onedelay.boostcampassignment.utils

import android.content.Context
import android.net.ConnectivityManager

object Utils {
    fun isNetworkConnected(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.activeNetworkInfo != null
    }
}