package com.example.androidchallenge.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.example.androidchallenge.data.model.SeriesDetails
import com.example.androidchallenge.data.response.Result
import com.example.androidchallenge.data.source.local.FavoritesDataSource
import com.example.androidchallenge.data.source.remote.TvMazeDataSource
import com.hermosodev.plantitas.MainCoroutineRule
import com.hermosodev.plantitas.observeForTesting
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyInt
import org.mockito.junit.MockitoJUnitRunner

@FlowPreview
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
class ExchangeRepositoryTest {
    private lateinit var repository: SeriesRepository

    private val tvMazeDataSource = mock<TvMazeDataSource>()
    private val favoritesDataSource = mock<FavoritesDataSource>()

    private val series = mock<SeriesDetails>()

    @get:Rule
    var testCoroutineDispatcher = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        repository = SeriesRepositoryImpl(
            testCoroutineDispatcher.testDispatcher,
            favoritesDataSource,
            tvMazeDataSource
        )
    }

    @Test
    fun getSeriesDetails_success() = testCoroutineDispatcher.testDispatcher.runBlockingTest {
        `when`(tvMazeDataSource.getSeriesDetails(anyInt()))
            .thenReturn(flow {
                delay(10)
                emit(series)
            })

        val liveData = repository.getSeriesDetails(1).asLiveData()
        liveData.observeForTesting {
            assertEquals(Result.loading<SeriesDetails>(), liveData.value)
            testCoroutineDispatcher.testDispatcher.advanceTimeBy(10)
            assertEquals(Result.success(series), liveData.value)
        }
    }

    @Test
    fun getSeriesDetails_failure() = testCoroutineDispatcher.testDispatcher.runBlockingTest {
        `when`(tvMazeDataSource.getSeriesDetails(anyInt()))
            .thenReturn(flow {
                delay(10)
                throw Exception("No connection")
            })

        val liveData = repository.getSeriesDetails(1).asLiveData()
        liveData.observeForTesting {
            assertEquals(Result.loading<SeriesDetails>(), liveData.value)
            testCoroutineDispatcher.testDispatcher.advanceTimeBy(10)
            assertEquals(
                Result.error<SeriesDetails>("No connection"),
                liveData.value
            )
        }
    }
}