package com.example.androidchallenge.ui.episode.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.androidchallenge.data.model.EpisodeDetails
import com.example.androidchallenge.data.repository.SeriesRepository
import com.example.androidchallenge.data.response.Result
import com.example.androidchallenge.ui.series.fragment.EpisodeDetailsFragmentArgs
import com.example.androidchallenge.util.ArgsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val seriesRepository: SeriesRepository
) : ArgsViewModel(savedStateHandle) {
    private val args: EpisodeDetailsFragmentArgs by navArgs()

    private var job: Job? = null

    private val _episode = MutableLiveData<Result<EpisodeDetails>>(Result.loading())
    val episode: LiveData<Result<EpisodeDetails>>; get() = _episode

    init {
        loadEpisode()
    }

    fun loadEpisode() {
        job?.apply { if (isActive) cancel() }
        job = viewModelScope.launch {
            seriesRepository.getEpisodeDetails(args.eid).collect {
                _episode.value = it
            }
        }
    }
}