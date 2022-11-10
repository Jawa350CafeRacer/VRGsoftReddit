package com.example.vrgsoftreddit.data.repository

import com.example.vrgsoftreddit.data.RetrofitInstance
import com.example.vrgsoftreddit.model.JsonReddit

class Repository {
    suspend fun getD(after: String?): JsonReddit {
        return RetrofitInstance.api.getData(after)
    }
}