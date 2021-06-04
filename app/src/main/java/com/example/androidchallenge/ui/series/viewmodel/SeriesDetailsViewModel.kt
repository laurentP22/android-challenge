package com.example.androidchallenge.ui.series.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.androidchallenge.data.model.Season
import com.example.androidchallenge.data.model.SeriesDetails
import com.example.androidchallenge.data.repository.SeriesRepository
import com.example.androidchallenge.data.response.Result
import com.example.androidchallenge.ui.series.fragment.SeriesDetailsFragmentArgs
import com.example.androidchallenge.util.ArgsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val seriesRepository: SeriesRepository
) : ArgsViewModel(savedStateHandle) {
    private val args: SeriesDetailsFragmentArgs by navArgs()

    private var job: Job? = null

    private val _details = MutableLiveData<Result<SeriesDetails>>(Result.loading())
    val details: LiveData<Result<SeriesDetails>>; get() = _details

    private val _seasons = MutableLiveData<Result<List<Season>>>(Result.loading())
    val seasons: LiveData<Result<List<Season>>>; get() = _seasons

    init {
        loadDetails()
    }

    fun loadDetails() {
        job?.apply { if (isActive) cancel() }
        job = viewModelScope.launch {
            val a = async {
                seriesRepository.getSeriesDetails(args.sid).collect {
                    _details.value = it
                }
            }
            val b = async {
                seriesRepository.getSeriesSeasons(args.sid).collect {
                    _seasons.value = it
                }
            }
            awaitAll(a, b)
        }
    }

    /**
     * Get the day of the fisrt episode broadcast.
     */
    fun getFirstBroadcast(seasons: List<Season>?): String? {
        return seasons?.let {
            if (it.isNotEmpty() && it[0].episodes.isNotEmpty())
                return it[0].episodes[0].airdate
            else
                null
        }
    }

    /**
     * Get the day of the last episode broadcast.
     */
    fun getLastBroadcast(seasons: List<Season>?): String? {
        return seasons?.let {
            when {
                it[seasons.lastIndex].episodes.isNotEmpty() -> it[seasons.lastIndex].episodes[it[seasons.lastIndex].episodes.lastIndex].airdate
                else -> null
            }
        }
    }

    /**
     * Get the total time of the series.
     */
    fun getTotalAirTime(seasons: List<Season>?): String? {
        return seasons?.let {
            val time = it.map { s ->
                s.episodes.map { e ->
                    e.runtime
                }.sum()
            }.sum()
            val hours: Int = time / 60
            val minutes: Int = time % 60
            return "${hours}h$minutes"
        }
    }
}