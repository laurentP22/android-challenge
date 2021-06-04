package com.example.androidchallenge.data.repository

import com.example.androidchallenge.data.model.*
import com.example.androidchallenge.data.response.Result
import kotlinx.coroutines.flow.Flow

interface SeriesRepository {

    /**
     * Gets user's favorites series.
     */
    suspend fun getFavorites(): Flow<List<Series>>

    /**
     * Adds or removes a series to the user's favorites.
     * @param series Series.
     * @param isFavorite False to remove, true to add.
     */
    suspend fun updateFavorite(series: Series, isFavorite: Boolean)

    /**
     * Gets all the series registered in tvmaze.
     * @param page Pagination
     */
    suspend fun getSeries(page: Int): Flow<Result<List<Series>>>

    /**
     * Gets all the series registered in tvmaze based on a query.
     * @param query Query
     */
    suspend fun searchSeries(query: String): Flow<Result<List<Series>>>

    /**
     * Gets all the details of a series.
     * @param sid Id of the series
     */
    suspend fun getSeriesDetails(sid: Int): Flow<Result<SeriesDetails>>

    /**
     * Gets all the seasons (with episodes) of a series.
     * @param sid Id of the series
     */
    suspend fun getSeriesSeasons(sid: Int): Flow<Result<List<Season>>>

    /**
     * Gets all the details of an episode.
     * @param eid Id of the episode
     */
    suspend fun getEpisodeDetails(eid: Int): Flow<Result<EpisodeDetails>>
}