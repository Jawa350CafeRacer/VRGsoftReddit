package com.example.vrgsoftreddit.data

import com.example.vrgsoftreddit.model.JsonReddit
import retrofit2.http.*

interface ApiService {

    @GET("top.json")
    suspend fun getData(
        @Query("after") after: String?,
        @Query("limit") limit: String = "25"
    ): JsonReddit
}