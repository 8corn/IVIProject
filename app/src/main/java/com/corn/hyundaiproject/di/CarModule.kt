package com.corn.hyundaiproject.di

import android.content.Context
import com.corn.hyundaiproject.data.car.CarPropertyDataSource
import com.corn.hyundaiproject.data.repository.CarRepositoryImpl
import com.corn.hyundaiproject.domain.usecase.GetTemperatureUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)   // 앱이 살아있는 동안 딱 하나씩만 유지
object CarModule {
    @Provides
    @Singleton
    fun provideCarPropertyDataSource(
        @ApplicationContext context: Context    // Hilt가 알아서 Context 넣어줌
    ): CarPropertyDataSource {
        return CarPropertyDataSource(context)
    }

    @Provides
    @Singleton
    fun provideCarRepository(
        dataSource: CarPropertyDataSource
    ): CarRepositoryImpl {
        return CarRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideGetTemperatureUseCase(
        repository: CarRepositoryImpl
    ): GetTemperatureUseCase {
        return GetTemperatureUseCase(repository)
    }
}