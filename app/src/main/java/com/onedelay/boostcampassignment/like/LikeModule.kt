package com.onedelay.boostcampassignment.like

import android.support.v7.app.AppCompatActivity
import com.onedelay.boostcampassignment.di.scope.ActivityScope
import com.onedelay.boostcampassignment.like.caseProvider.LikeCaseProvider
import com.onedelay.boostcampassignment.like.caseProvider.LikeCaseProviderApi
import com.onedelay.boostcampassignment.like.caseProvider.present.LikePresentCase
import com.onedelay.boostcampassignment.like.caseProvider.present.LikePresentCaseApi
import com.onedelay.boostcampassignment.like.caseProvider.source.LikeSourceCase
import com.onedelay.boostcampassignment.like.caseProvider.source.LikeSourceCaseApi
import com.onedelay.boostcampassignment.like.channel.LikeChannel
import com.onedelay.boostcampassignment.like.channel.LikeChannelApi
import com.onedelay.boostcampassignment.like.source.LikeDataSource
import com.onedelay.boostcampassignment.like.source.LikeDataSourceApi
import com.onedelay.boostcampassignment.like.view.LikeActivity
import com.onedelay.boostcampassignment.like.view.LikeView
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference


@Module(includes = [LikeModule.ProvideModule::class])
internal interface LikeModule {
    @Module
    class ProvideModule {
        @Provides
        @ActivityScope
        @LikeScreen
        fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

        @Provides
        @ActivityScope
        fun provideWeakView(@LikeScreen activity: AppCompatActivity): WeakReference<LikeView> {
            return WeakReference(activity as LikeView)
        }
    }

    @Binds
    @ActivityScope
    @LikeScreen
    fun provideActivity(activity: LikeActivity): AppCompatActivity

    @Binds
    @ActivityScope
    fun provideLikeChannel(channel: LikeChannel): LikeChannelApi

    @Binds
    @ActivityScope
    fun provideLikeDataSource(dataSource: LikeDataSource): LikeDataSourceApi

    @Binds
    @ActivityScope
    fun provideCaseProvider(caseProvider: LikeCaseProvider): LikeCaseProviderApi

    @Binds
    @ActivityScope
    fun providePresentCase(presentCase: LikePresentCase): LikePresentCaseApi

    @Binds
    @ActivityScope
    fun provideSourceCase(sourceCase: LikeSourceCase): LikeSourceCaseApi
}