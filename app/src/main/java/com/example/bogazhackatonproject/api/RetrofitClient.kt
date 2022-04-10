package com.example.bogazhackatonproject.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URl = "http://farmai.pagekite.me/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URl).build()

object BogazRetrofit{
    val retrofitService:BogazApi by lazy { retrofit.create(BogazApi::class.java) }
}