package com.example.androidchallenge.ui.main.viewmodel

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidchallenge.data.model.Series
import com.example.androidchallenge.data.repository.SeriesRepository
import com.example.androidchallenge.data.response.Result
import com.example.androidchallenge.data.response.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val seriesRepository: SeriesRepository
) : ViewModel() {
    private var job: Job? = null

    private val _series = MutableLiveData<Result<List<Series>>>(Result.loading())
    val series: LiveData<Result<List<Series>>>; get() = _series

    private var _page: Int = 0
    private var _endReached: Boolean = false

    // Variable used to save the scrolling state of the page
    var scrollingState: Parcelable? = null

    init {
        loadSeries()
    }

    /**
     * Loads TvMaze main series.
     */
    fun loadSeries() {
        _page = 0

        job?.apply { if (isActive) cancel() }
        job = viewModelScope.launch {
            seriesRepository.getSeries(_page).collect { _series.value = it }
        }
    }

    /**
     * Loads next page (works only if the query is null or blank).
     */
    fun loadNextPage() {
        if (_endReached) {
            return
        }

        _page++

        job?.apply { if (isActive) cancel() }
        job = viewModelScope.launch {
            seriesRepository.getSeries(_page).collect {
                val currentSeries = _series.value?.data ?: listOf()
                when (it.status) {
                    Status.LOADING -> {
                        _series.value = Result(Status.LOADING, currentSeries, null)
                    }
                    Status.ERROR -> {
                        _page--
                        _series.value = Result.error(it.message)
                    }
                    Status.SUCCESS -> {
                        val newSeries = it.data ?: listOf()
                        _endReached = newSeries.isEmpty()
                        _series.value = Result.success(currentSeries + newSeries)
                    }
                }
            }
        }
    }
}