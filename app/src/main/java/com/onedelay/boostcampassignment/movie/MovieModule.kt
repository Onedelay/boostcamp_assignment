package com.onedelay.boostcampassignment.movie

import android.support.v7.app.AppCompatActivity
import com.onedelay.boostcampassignment.di.scope.ActivityScope
import com.onedelay.boostcampassignment.movie.caseProvider.MovieCaseProvider
import com.onedelay.boostcampassignment.movie.caseProvider.MovieCaseProviderApi
import com.onedelay.boostcampassignment.movie.caseProvider.present.MoviePresentCaseApi
import com.onedelay.boostcampassignment.movie.caseProvider.present.MoviePresenterCase
import com.onedelay.boostcampassignment.movie.caseProvider.source.MovieSourceCase
import com.onedelay.boostcampassignment.movie.caseProvider.source.MovieSourceCaseApi
import com.onedelay.boostcampassignment.movie.channel.MovieChannel
import com.onedelay.boostcampassignment.movie.channel.MovieChannelApi
import com.onedelay.boostcampassignment.movie.source.MovieDataSource
import com.onedelay.boostcampassignment.movie.source.MovieDataSourceApi
import com.onedelay.boostcampassignment.movie.view.MovieActivity
import com.onedelay.boostcampassignment.movie.view.MovieView
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference


@Module(
        includes = [
            MovieModule.ProvideModule::class
        ]
)
internal interface MovieModule {
    @Module
    class ProvideModule {
        @Provides
        @ActivityScope
        @MovieScreen
        fun provideCompositeDisposable(): CompositeDisposable {
            return CompositeDisposable()
        }

        @Provides
        @ActivityScope
        fun provideWeakView(@MovieScreen activity: AppCompatActivity): WeakReference<MovieView> {
            return WeakReference(activity as MovieView)
        }
    }

    @Binds
    @ActivityScope
    @MovieScreen
    fun provideActivity(activity: MovieActivity): AppCompatActivity

    @Binds
    @ActivityScope
    fun provideCaseProvider(caseProvider: MovieCaseProvider): MovieCaseProviderApi

    @Binds
    @ActivityScope
    fun providePresentCase(presentCase: MoviePresenterCase): MoviePresentCaseApi

    @Binds
    @ActivityScope
    fun provideSourceCase(sourceCase: MovieSourceCase): MovieSourceCaseApi

    @Binds
    @ActivityScope
    fun provideDataSource(dateSource: MovieDataSource): MovieDataSourceApi

    @Binds
    @ActivityScope
    fun provideChannel(channel: MovieChannel): MovieChannelApi
}