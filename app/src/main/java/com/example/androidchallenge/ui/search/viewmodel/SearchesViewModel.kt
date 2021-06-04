package com.example.androidchallenge.ui.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.androidchallenge.data.model.Series
import com.example.androidchallenge.data.repository.SeriesRepository
import com.example.androidchallenge.data.response.Result
import com.example.androidchallenge.ui.search.fragment.SearchesFragmentArgs
import com.example.androidchallenge.util.ArgsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val seriesRepository: SeriesRepository
) : ArgsViewModel(savedStateHandle) {
    private val args: SearchesFragmentArgs by navArgs()

    private var job: Job? = null

    private val _series = MutableLiveData<Result<List<Series>>>(Result.loading())
    val series: LiveData<Result<List<Series>>>; get() = _series

    init {
        searchSeries()
    }

    fun searchSeries() {
        job?.apply { if (isActive) cancel() }
        job = viewModelScope.launch {
            seriesRepository.searchSeries(args.query).collect {
                _series.value = it
            }
        }
    }
}