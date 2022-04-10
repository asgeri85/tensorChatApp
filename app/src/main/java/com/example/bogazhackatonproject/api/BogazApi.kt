package com.example.bogazhackatonproject.api

import com.example.bogazhackatonproject.model.ResponseModel
import retrofit2.http.GET

interface BogazApi {

    @GET("cikti.json")
    suspend fun getValue(): ResponseModel

}