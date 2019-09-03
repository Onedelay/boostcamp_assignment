package com.onedelay.boostcampassignment.data.source

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


internal object RetrofitApi {

    private const val BASE_URL = "https://openapi.naver.com/"

    val service: RetrofitService

    init {
        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        service = retrofit.create(RetrofitService::class.java)
    }

}