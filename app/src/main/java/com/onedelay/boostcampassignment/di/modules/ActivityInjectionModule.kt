package com.onedelay.boostcampassignment.di.modules

import com.onedelay.boostcampassignment.di.scope.ActivityScope
import com.onedelay.boostcampassignment.liked.LikedMovieActivity
import com.onedelay.boostcampassignment.liked.LikedMovieModule
import com.onedelay.boostcampassignment.main.MainActivity
import com.onedelay.boostcampassignment.main.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
internal interface ActivityInjectionModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    fun mainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [LikedMovieModule::class])
    fun likedMovieActivity(): LikedMovieActivity
}