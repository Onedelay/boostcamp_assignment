package com.onedelay.boostcampassignment.di.modules

import com.onedelay.boostcampassignment.di.scope.ActivityScope
import com.onedelay.boostcampassignment.like.LikeActivity
import com.onedelay.boostcampassignment.like.LikeModule
import com.onedelay.boostcampassignment.liked.LikedMovieActivity
import com.onedelay.boostcampassignment.liked.LikedMovieModule
import com.onedelay.boostcampassignment.main.MainActivity
import com.onedelay.boostcampassignment.main.MainModule
import com.onedelay.boostcampassignment.movie.MovieActivity
import com.onedelay.boostcampassignment.movie.MovieModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
internal interface ActivityInjectionModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [LikedMovieModule::class])
    fun likedMovieActivity(): LikedMovieActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MovieModule::class])
    fun movieActivity(): MovieActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [LikeModule::class])
    fun likeActivity(): LikeActivity
}