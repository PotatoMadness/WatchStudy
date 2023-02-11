package com.example.testevinfra

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StationRepository {
    private val service: StationService

    init {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logger)
            .build()

        service = Retrofit.Builder()
            .baseUrl("https://dev.soft-berry.co.kr")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StationService::class.java)
    }

    suspend fun getStationList(latitude: Double, longitude: Double): Result<List<StationInfo>> {
        kotlin.runCatching {
            service.getStationList(latitude.toString(), longitude.toString())
        }.onSuccess {
            return Result.success(it.list)
        }.onFailure {
            return Result.failure(it)
        }.also {
            return Result.failure(Exception("정보를 가져오지 못함"))
        }
    }
}

