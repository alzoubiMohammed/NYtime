package com.deloitte.mostpopular.data.network

import com.deloitte.mostpopular.data.dto.NewsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface MostPopularEndPoint {
    @GET("viewed/{period}.json")
   suspend fun getMostViewByPeriod(@Path("period") period:String): NewsDto
}