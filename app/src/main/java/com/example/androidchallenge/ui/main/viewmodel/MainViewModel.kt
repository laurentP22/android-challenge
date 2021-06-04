package com.example.androidchallenge.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidchallenge.data.model.Series
import com.example.androidchallenge.data.repository.SeriesRepository
import com.example.androidchallenge.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val seriesRepository: SeriesRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private var _favoritesJob: Job? = null

    private val _auth = MutableLiveData<String>()
    val auth: LiveData<String>; get() = _auth

    private val _favorites = MutableLiveData<ArrayList<Series>>()
    val favorites: LiveData<ArrayList<Series>>; get() = _favorites

    init {
        viewModelScope.launch {
            getUserAuth()
            initFavoriteListener()
        }
    }

    fun getUserAuth(){
        viewModelScope.launch {
            _auth.value = userRepository.getAuthMode()
        }
    }

    /**
     * Starts listening to the user's favorites series.
     */
    private suspend fun initFavoriteListener() {
        _favoritesJob?.apply { if (isActive) cancel() }
        _favoritesJob = viewModelScope.launch {
            seriesRepository.getFavorites().collect {
                _favorites.value = ArrayList(it)
            }
        }
    }

    /**
     * Updates the user's favorites.
     */
    fun updateFavorite(series: Series) {
        val bool = _favorites.value?.filter { it.id == series.id }.isNullOrEmpty()
        viewModelScope.launch { seriesRepository.updateFavorite(series, bool) }
    }
}