package com.example.androidchallenge.di

import android.content.Context
import androidx.room.Room
import com.example.androidchallenge.data.repository.SeriesRepository
import com.example.androidchallenge.data.repository.SeriesRepositoryImpl
import com.example.androidchallenge.data.repository.UserRepository
import com.example.androidchallenge.data.repository.UserRepositoryImpl
import com.example.androidchallenge.data.source.local.FavoritesDao
import com.example.androidchallenge.data.source.local.FavoritesDataSource
import com.example.androidchallenge.data.source.local.FavoritesDatabase
import com.example.androidchallenge.data.source.local.SharedPreferenceDataSource
import com.example.androidchallenge.data.source.remote.TvMazeDataSource
import com.example.androidchallenge.util.VolleySingleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun volleySingleTon(@ApplicationContext appContext: Context): VolleySingleton =
        VolleySingleton.getInstance(appContext)

    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): FavoritesDatabase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            FavoritesDatabase::class.java, "FavoritesDatabase"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideAddressDao(database: FavoritesDatabase): FavoritesDao {
        return database.favoritesDao()
    }
}

/**
 * The binding for PlantRepository is on its own module so that we can replace it easily in tests.
 */
@Module
@InstallIn(SingletonComponent::class)
object AndroidChallengeModule {

    @Singleton
    @Provides
    fun provideSeriesRepository(
        tvMazeDataSource: TvMazeDataSource,
        ioDispatcher: CoroutineDispatcher,
        favoritesDataSource: FavoritesDataSource
    ): SeriesRepository =
        SeriesRepositoryImpl(ioDispatcher, favoritesDataSource, tvMazeDataSource)

    @Singleton
    @Provides
    fun provideUserRepository(
        sharedPreferenceDataSource: SharedPreferenceDataSource
    ): UserRepository = UserRepositoryImpl(sharedPreferenceDataSource)
}
