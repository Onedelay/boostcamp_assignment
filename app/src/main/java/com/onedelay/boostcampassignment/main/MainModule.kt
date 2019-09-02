package com.onedelay.boostcampassignment.main

import com.onedelay.boostcampassignment.data.MainRepositoryApi
import com.onedelay.boostcampassignment.data.MovieListRepository
import com.onedelay.boostcampassignment.di.scope.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference


@Module(
        includes = [
            MainModule.ProvideModule::class
        ]
)
internal interface MainModule {
    @Module
    class ProvideModule {
        @Provides
        @ActivityScope
        fun provideCompositeDisposable(): CompositeDisposable {
            return CompositeDisposable()
        }

        @Provides
        @ActivityScope
        fun provideMainActivity(activity: MainActivity): WeakReference<MainContract.View> {
            return WeakReference(activity)
        }

        @Provides
        @ActivityScope
        fun provideMovieListAdapter(): MovieAdapter {
            return MovieAdapter()
        }
    }

    @Binds
    @ActivityScope
    fun provideMainPresenter(presenter: MainPresenter): MainContract.Presenter

    @Binds
    @ActivityScope
    fun provideRepository(repository: MovieListRepository): MainRepositoryApi
}