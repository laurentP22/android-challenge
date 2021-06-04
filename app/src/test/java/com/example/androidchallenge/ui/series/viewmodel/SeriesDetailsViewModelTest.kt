package com.example.androidchallenge.ui.series.viewmodel

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.androidchallenge.constant.Constants.Companion.BUNDLE_ARGS
import com.example.androidchallenge.data.model.Season
import com.example.androidchallenge.data.model.SeriesDetails
import com.example.androidchallenge.data.repository.SeriesRepository
import com.example.androidchallenge.data.response.Result
import com.example.androidchallenge.data.response.Status
import com.hermosodev.plantitas.MainCoroutineRule
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(MockitoJUnitRunner::class)
class SeriesDetailsViewModelTest {
    lateinit var viewModel: SeriesDetailsViewModel

    private val seriesRepository = mock<SeriesRepository>()

    private val seriesDetails = mock<SeriesDetails>()
    private val seasons1 = mock<Season>()
    private val seasons2 = mock<Season>()

    private val channelSeries = ConflatedBroadcastChannel<Result<SeriesDetails>>()
    private val channelSeasons = ConflatedBroadcastChannel<Result<List<Season>>>()

    private val bundle = mock<Bundle>()

    @get:Rule
    var testCoroutineDispatcher = MainCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() = testCoroutineDispatcher.runBlockingTest {
        Mockito.`when`(seriesRepository.getSeriesDetails(anyInt()))
            .thenReturn(channelSeries.asFlow())
        Mockito.`when`(seriesRepository.getSeriesSeasons(anyInt()))
            .thenReturn(channelSeasons.asFlow())

        Mockito.`when`(bundle.containsKey("sid")).thenReturn(true)
        Mockito.`when`(bundle.getInt(anyString())).thenReturn(1)
        val savedStateHandle = SavedStateHandle().apply {
            set(BUNDLE_ARGS, bundle)
        }
        viewModel = SeriesDetailsViewModel(savedStateHandle, seriesRepository)
    }

    @Test
    fun loadDetails_success() = testCoroutineDispatcher.runBlockingTest {
        Mockito.`when`(seriesRepository.getSeriesDetails(anyInt()))
            .thenReturn(flow {
                emit(Result.loading())
                delay(10)
                emit(Result.success(seriesDetails))
            })
        Mockito.`when`(seriesRepository.getSeriesSeasons(anyInt()))
            .thenReturn(flow {
                emit(Result.loading())
                delay(10)
                emit(Result.success(listOf(seasons1, seasons2)))
            })

        viewModel.loadDetails()

        // Loading
        assertEquals(Status.LOADING, viewModel.details.value?.status)
        assertEquals(Status.LOADING, viewModel.seasons.value?.status)

        testCoroutineDispatcher.advanceTimeBy(10)
        // Success
        assertEquals(Result.success(seriesDetails), viewModel.details.value)
        assertEquals(Result.success(listOf(seasons1, seasons2)), viewModel.seasons.value)
    }

    @Test
    fun loadDetails_failure() = testCoroutineDispatcher.runBlockingTest {
        Mockito.`when`(seriesRepository.getSeriesDetails(anyInt()))
            .thenReturn(flow {
                emit(Result.loading())
                delay(10)
                emit(Result.error<SeriesDetails>("No Connection"))
            })
        Mockito.`when`(seriesRepository.getSeriesSeasons(anyInt()))
            .thenReturn(flow {
                emit(Result.loading())
                delay(10)
                emit(Result.error<List<Season>>("Error"))
            })
        viewModel.loadDetails()

        // Loading
        assertEquals(Status.LOADING, viewModel.details.value?.status)
        assertEquals(Status.LOADING, viewModel.seasons.value?.status)

        testCoroutineDispatcher.advanceTimeBy(10)

        // Success
        assertEquals(Status.ERROR, viewModel.details.value?.status)
        assertEquals(Status.ERROR, viewModel.seasons.value?.status)
    }
}