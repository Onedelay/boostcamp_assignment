package com.onedelay.boostcampassignment.like

import android.arch.lifecycle.ViewModel
import com.onedelay.boostcampassignment.di.ViewModelKey
import com.onedelay.boostcampassignment.di.scope.ActivityScope
import com.onedelay.boostcampassignment.like.channel.LikeChannel
import com.onedelay.boostcampassignment.like.channel.LikeChannelApi
import com.onedelay.boostcampassignment.like.repository.LikeRepository
import com.onedelay.boostcampassignment.like.repository.LikeRepositoryApi
import com.onedelay.boostcampassignment.like.source.LikeDataSource
import com.onedelay.boostcampassignment.like.source.LikeDataSourceApi
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module(includes = [LikeModule.ProvideModule::class])
internal interface LikeModule {
    @Module
    class ProvideModule

    @Binds
    @ActivityScope
    @IntoMap
    @ViewModelKey(LikeViewModel::class)
    fun provideLikeViewModel(viewModel: LikeViewModel): ViewModel

    @Binds
    @ActivityScope
    fun provideLikeChannel(channel: LikeChannel): LikeChannelApi

    @Binds
    @ActivityScope
    fun provideLikeRepository(repository: LikeRepository): LikeRepositoryApi

    @Binds
    @ActivityScope
    fun provideLikeDataSource(dataSource: LikeDataSource): LikeDataSourceApi
}