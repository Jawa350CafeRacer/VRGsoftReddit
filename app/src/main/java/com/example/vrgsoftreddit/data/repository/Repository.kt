package com.example.vrgsoftreddit.data.repository

import com.example.vrgsoftreddit.data.RetrofitInstance
import com.example.vrgsoftreddit.model.JsonReddit

class Repository {
    suspend fun getD(after: String?): Result<JsonReddit> {
        val response = RetrofitInstance.api.getData(after)
        return try  {
            Result.success(response)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}