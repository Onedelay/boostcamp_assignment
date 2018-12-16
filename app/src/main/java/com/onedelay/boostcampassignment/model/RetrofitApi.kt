package com.onedelay.boostcampassignment.model

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApi {
    private const val BASE_URL = "https://openapi.naver.com/"
    val service: RetrofitService

    init {
        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.d("API Log", message) })
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        service = retrofit.create(RetrofitService::class.java)
    }


}