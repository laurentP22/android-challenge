package com.example.androidchallenge.di

import com.example.androidchallenge.data.repository.SeriesRepository
import com.example.androidchallenge.data.repository.UserRepository
import com.example.androidchallenge.data.repository.UserRepositoryImpl
import com.example.androidchallenge.data.source.FakeSeriesRepository
import com.example.androidchallenge.data.source.FakeUserRepository
import com.example.androidchallenge.data.source.local.SharedPreferenceDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Repositories binding to use in tests.
 *
 * Hilt will inject a [FakeSeriesRepository] instead of a [SeriesRepository].
 * Hilt will inject a [FakeUserRepository] instead of a [UserRepository].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class TestModule {
    @Singleton
    @Binds
    abstract fun bindSeriesRepository(seriesRepository: FakeSeriesRepository): SeriesRepository

    @Singleton
    @Binds
    abstract fun bindUserRepository(userRepository: FakeUserRepository): UserRepository
}
