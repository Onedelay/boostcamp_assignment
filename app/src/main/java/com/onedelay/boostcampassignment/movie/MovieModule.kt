package com.onedelay.boostcampassignment.movie

import android.arch.lifecycle.ViewModel
import com.onedelay.boostcampassignment.di.ViewModelKey
import com.onedelay.boostcampassignment.di.scope.ActivityScope
import com.onedelay.boostcampassignment.movie.channel.MovieChannel
import com.onedelay.boostcampassignment.movie.channel.MovieChannelApi
import com.onedelay.boostcampassignment.movie.repository.MovieRepository
import com.onedelay.boostcampassignment.movie.repository.MovieRepositoryApi
import com.onedelay.boostcampassignment.movie.source.MovieDataSource
import com.onedelay.boostcampassignment.movie.source.MovieDataSourceApi
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module(
        includes = [
            MovieModule.ProvideModule::class
        ]
)
internal interface MovieModule {
    @Module
    class ProvideModule

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    fun provideMovieViewModel(viewModel: MovieViewModel): ViewModel

    @Binds
    @ActivityScope
    fun provideRepository(repository: MovieRepository): MovieRepositoryApi

    @Binds
    @ActivityScope
    fun provideDataSource(dateSource: MovieDataSource): MovieDataSourceApi

    @Binds
    @ActivityScope
    fun provideChannel(channel: MovieChannel): MovieChannelApi
}