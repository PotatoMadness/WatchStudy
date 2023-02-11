package com.example.testevinfra.di

import android.content.Context
import android.os.Looper
import com.example.testevinfra.LocationUpdatesUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class LocationUpdatesUseCaseModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LocationProviderClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainLooper

    @Provides
    @Singleton
    @LocationProviderClient
    fun providesLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    @MainLooper
    fun providesMainLooper(@ApplicationContext context: Context): Looper = context.mainLooper

    @Provides
    @Singleton
    fun provideLocationUpdatesUseCase(
        @LocationProviderClient client: FusedLocationProviderClient,
        @MainLooper looper: Looper,
    ): LocationUpdatesUseCase = LocationUpdatesUseCase(client, looper)
}