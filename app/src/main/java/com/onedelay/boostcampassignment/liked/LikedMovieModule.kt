package com.onedelay.boostcampassignment.liked

import com.onedelay.boostcampassignment.di.scope.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.lang.ref.WeakReference

@Module(
        includes = [
            LikedMovieModule.ProvideModule::class
        ]
)
internal interface LikedMovieModule {
    @Module
    class ProvideModule {
        @Provides
        @ActivityScope
        fun provideLikedActivity(activity: LikedMovieActivity): WeakReference<LikedMovieContract.View> {
            return WeakReference(activity)
        }

        @Provides
        @ActivityScope
        fun provideLikedMovieAdapter(): LikedMovieAdapter {
            return LikedMovieAdapter()
        }
    }

    @Binds
    @ActivityScope
    fun provideLikedMoviePresenter(likedMoviePresenter: LikedMoviePresenter): LikedMovieContract.Presenter
}