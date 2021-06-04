package com.example.androidchallenge.data.repository

import com.example.androidchallenge.data.model.Season
import com.example.androidchallenge.data.model.Series
import com.example.androidchallenge.data.response.Result
import com.example.androidchallenge.data.source.local.FavoritesDataSource
import com.example.androidchallenge.data.source.remote.TvMazeDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SeriesRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val favoritesDataSource: FavoritesDataSource,
    private val TvMazeDataSource: TvMazeDataSource
) : SeriesRepository {

    override suspend fun getFavorites() = favoritesDataSource.getAll().flowOn(ioDispatcher)

    override suspend fun updateFavorite(series: Series, isFavorite: Boolean) {
        if (isFavorite) {
            favoritesDataSource.add(series)
        } else {
            favoritesDataSource.delete(series.id)
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getSeries(page: Int) = flow {
        emit(Result.loading())
        TvMazeDataSource.getSeries(page).collect {
            emit(Result.success(it))
        }
    }.catch {
        emit(Result.error(it.message))
    }.flowOn(ioDispatcher)

    @ExperimentalCoroutinesApi
    override suspend fun searchSeries(query: String) = flow {
        emit(Result.loading())
        TvMazeDataSource.searchSeries(query).collect {
            emit(Result.success(it))
        }
    }.catch {
        emit(Result.error(it.message))
    }.flowOn(ioDispatcher)


    @ExperimentalCoroutinesApi
    override suspend fun getSeriesDetails(sid: Int) = flow {
        emit(Result.loading())
        TvMazeDataSource.getSeriesDetails(sid).collect {
            emit(Result.success(it))
        }
    }.catch {
        emit(Result.error(it.message))
    }.flowOn(ioDispatcher)

    @ExperimentalCoroutinesApi
    override suspend fun getSeriesSeasons(sid: Int) = flow {
        emit(Result.loading())
        TvMazeDataSource.getSeriesEpisodes(sid).collect {
            val totalSeasons = it.map { e -> e.season }.maxOrNull() ?: 0
            val seasons = arrayListOf<Season>()
            for (i in 1..totalSeasons) {
                val season = Season(i, it.filter { e -> e.season == i }.toList())
                seasons.add(season)
            }
            emit(Result.success(seasons))
        }
    }.catch {
        emit(Result.error(it.message))
    }.flowOn(ioDispatcher)

    @ExperimentalCoroutinesApi
    override suspend fun getEpisodeDetails(eid: Int) = flow {
        emit(Result.loading())
        TvMazeDataSource.getEpisodeDetails(eid).collect {
            emit(Result.success(it))
        }
    }.catch {
        emit(Result.error(it.message))
    }.flowOn(ioDispatcher)
}