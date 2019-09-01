package com.onedelay.boostcampassignment

import com.crashlytics.android.Crashlytics
import com.onedelay.boostcampassignment.di.DaggerMovieComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric


internal class MovieApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        Fabric.with(this, Crashlytics())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerMovieComponent.builder().application(this).build()
    }

}