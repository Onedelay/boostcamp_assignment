package com.onedelay.boostcampassignment.data.source

import com.onedelay.boostcampassignment.data.dto.MovieList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface RetrofitService {

    /** Http 헤더에 넣는 정보가 RetrofitService에 코드로 박혀있음.
     지금은 문제 없지만 Real / Beta / Dev 식으로 다른 키 값을 사용하는 경우가 있기 때문에 외부에서 관리하는 것이 바람직함. **/
    @Headers("X-Naver-Client-Id: 1a8GYU3yLDmzss142rsx", "X-Naver-Client-Secret: hZjZjbMr5R")
    @GET("/v1/search/movie.json")
    fun requestMovieInfo(@Query("query") query: String, @Query("start") start: Int): Single<MovieList>

}
