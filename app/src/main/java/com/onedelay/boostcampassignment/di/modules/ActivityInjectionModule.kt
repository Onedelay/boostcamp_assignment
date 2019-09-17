package com.onedelay.boostcampassignment.di.modules

import com.onedelay.boostcampassignment.di.scope.ActivityScope
import com.onedelay.boostcampassignment.like.LikeActivity
import com.onedelay.boostcampassignment.like.LikeModule
import com.onedelay.boostcampassignment.movie.MovieModule
import com.onedelay.boostcampassignment.movie.view.MovieActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
internal interface ActivityInjectionModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [MovieModule::class])
    fun movieActivity(): MovieActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [LikeModule::class])
    fun likeActivity(): LikeActivity
}