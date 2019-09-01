package com.onedelay.boostcampassignment.di

import com.onedelay.boostcampassignment.data.InMemoryDataHolder
import com.onedelay.boostcampassignment.data.source.RetrofitApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(
        includes = [
            ApplicationModule.ProvideModule::class
        ]
)
internal interface ApplicationModule {
    @Module
    class ProvideModule {
        @Provides
        @Singleton
        fun provideRetrofitApi(): RetrofitApi {
            return RetrofitApi
        }

        @Provides
        @Singleton
        fun provideInMemoryDataHolder(): InMemoryDataHolder {
            return InMemoryDataHolder
        }
    }
}