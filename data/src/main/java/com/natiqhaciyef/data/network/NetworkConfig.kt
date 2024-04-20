package com.natiqhaciyef.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object NetworkConfig {
    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val logger = OkHttpClient.Builder().addInterceptor(interceptor)
    val BASE_URL = "https://techtive.tech/wit/"
//    const val API_KEY = "30e171ab-3e37-4a9e-b146-5965922caf97"
}