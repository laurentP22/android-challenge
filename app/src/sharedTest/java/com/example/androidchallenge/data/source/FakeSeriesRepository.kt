package com.example.androidchallenge.data.source

import com.example.androidchallenge.data.model.*
import com.example.androidchallenge.data.repository.SeriesRepository
import com.example.androidchallenge.data.response.Result
import com.example.androidchallenge.data.response.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeSeriesRepository @Inject constructor() : SeriesRepository {
    var status = Status.LOADING
    override suspend fun getFavorites()= flow<List<Series>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateFavorite(series: Series, isFavorite: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getSeries(page: Int)= flow<Result<List<Series>>> {
        TODO("Not yet implemented")
    }

    override suspend fun searchSeries(query: String): Flow<Result<List<Series>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSeriesDetails(sid: Int) = flow<Result<SeriesDetails>> {
        when (status) {
            Status.LOADING -> emit(Result.loading())
            Status.SUCCESS -> emit(
                Result.success(
                    SeriesDetails(
                        id = 0,
                        name = "Vikings",
                        image = null,
                        genres = listOf("Action, Adventure"),
                        summary = "Vikings fighting in the woods"
                    )
                )
            )
            Status.ERROR -> emit(Result.error(""))
        }
    }

    override suspend fun getSeriesSeasons(sid: Int) = flow<Result<List<Season>>> {
        val seasons = listOf(
            Season(
                number = 1,
                episodes = listOf(
                    Episode(
                        id = 1,
                        name = "Episode1",
                        image = null,
                        season = 1,
                        number = 1,
                        airdate = "2012-01-20",
                        runtime = 60
                    ),
                    Episode(
                        id = 2,
                        name = "Episode2",
                        image = null,
                        season = 1,
                        number = 2,
                        airdate = "2012-01-27",
                        runtime = 60
                    )
                )
            ),
            Season(
                number = 2,
                episodes = listOf(
                    Episode(
                        id = 3,
                        name = "Episode1",
                        image = null,
                        season = 2,
                        number = 1,
                        airdate = "2013-04-13",
                        runtime = 60
                    ),
                    Episode(
                        id = 4,
                        name = "Episode2",
                        image = null,
                        season = 2,
                        number = 2,
                        airdate = "2013-04-21",
                        runtime = 60
                    )
                )
            ),
        )
        when (status) {
            Status.LOADING -> emit(Result.loading())
            Status.SUCCESS -> emit(
                Result.success(seasons)
            )
            Status.ERROR -> emit(Result.error(""))
        }
    }

    override suspend fun getEpisodeDetails(eid: Int): Flow<Result<EpisodeDetails>> {
        TODO("Not yet implemented")
    }

}