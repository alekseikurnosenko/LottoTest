package com.example.lottotest.lotteries

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

data class DrawInfo(
    val drawIdentifier: String,
    val drawResult: DrawResult?
)

data class DrawResult(
    val numbers: List<Int>,
    val superNumber: Int? = null,
    val euroNumbers: List<Int>? = null
)

data class DrawInfoListItem(
    val drawIdentifier: String
)

interface DrawInfoApi {
    @GET("/drawinfo/{lotteryId}/draws?past=10&future=0")
    fun drawsList(@Path("lotteryId") lotteryId: String): Observable<List<DrawInfoListItem>>

    @GET("/drawinfo/{lotteryId}/{drawIdentifier}")
    fun drawInfoById(
        @Path("lotteryId") lotteryId: String,
        @Path("drawIdentifier") drawIdentifier: String
    ): Observable<DrawInfo>

}