package com.example.testevinfra

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StationRepository {
    private val service: StationService

    init {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY

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

    suspend fun getStationList(latitude: Double, longitude: Double): ResultStationInfo {
        kotlin.runCatching {
            service.getStationList(latitude.toString(), longitude.toString())
        }.onSuccess {
            return ResultStationInfo.Success(it)
        }.onFailure {
            return ResultStationInfo.Failed(it)
        }.also {
            return ResultStationInfo.Failed(Exception("정보를 가져오지 못함"))
        }
    }
}

