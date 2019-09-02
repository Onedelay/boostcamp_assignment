package com.onedelay.boostcampassignment.di

import android.app.Application
import com.onedelay.boostcampassignment.MovieApplication
import com.onedelay.boostcampassignment.di.modules.ActivityInjectionModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton


@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            ApplicationModule::class,
            ActivityInjectionModule::class
        ]
)
internal interface MovieComponent : AndroidInjector<DaggerApplication> {
    fun inject(application: MovieApplication)

    /** Application 과 연결해주는 Builder 정의 */
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): MovieComponent
    }
}