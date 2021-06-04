package com.example.androidchallenge.data.source.remote

import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.androidchallenge.constant.Constants
import com.example.androidchallenge.data.model.*
import com.example.androidchallenge.util.VolleySingleton
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvMazeDataSource @Inject constructor(
    private val volleySingleton: VolleySingleton
) {

    @ExperimentalCoroutinesApi
    suspend fun getSeries(page: Int) = callbackFlow {
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, "${Constants.URL}/shows?page=$page", null,
            {
                val series = Gson().fromJson(it.toString(), Array<Series>::class.java).toList()
                offer(series)
            },
            {
                close(Exception(it.message))
            }
        )

        volleySingleton.addToRequestQueue(jsonObjectRequest)
        awaitClose { jsonObjectRequest.cancel() }
    }

    @ExperimentalCoroutinesApi
    suspend fun searchSeries(query: String) = callbackFlow {
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, "${Constants.URL}/search/shows?q=$query", null,
            {
                val result =
                    Gson().fromJson(it.toString(), Array<SearchSeriesResult>::class.java).toList()
                val series = result.map { search -> search.show }.toList()
                offer(series)
            },
            {
                close(Exception(it.message))
            }
        )

        volleySingleton.addToRequestQueue(jsonObjectRequest)
        awaitClose { jsonObjectRequest.cancel() }
    }

    @ExperimentalCoroutinesApi
    suspend fun getSeriesDetails(sid: Int) = callbackFlow<SeriesDetails> {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, "${Constants.URL}/shows/$sid", null,
            {
                val details = Gson().fromJson(
                    it.toString(),
                    SeriesDetails::class.java
                )
                offer(details)
            },
            {
                close(Exception(it.message))
            }
        )

        volleySingleton.addToRequestQueue(jsonObjectRequest)
        awaitClose { jsonObjectRequest.cancel() }
    }

    @ExperimentalCoroutinesApi
    suspend fun getSeriesEpisodes(sid: Int) = callbackFlow {
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, "${Constants.URL}/shows/$sid/episodes", null,
            {
                val episodes = Gson().fromJson(it.toString(), Array<Episode>::class.java).toList()
                offer(episodes)
            },
            {
                close(Exception(it.message))
            }
        )

        volleySingleton.addToRequestQueue(jsonObjectRequest)
        awaitClose { jsonObjectRequest.cancel() }
    }

    @ExperimentalCoroutinesApi
    suspend fun getEpisodeDetails(eid: Int) = callbackFlow {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, "${Constants.URL}/episodes/$eid", null,
            {
                val episode = Gson().fromJson(it.toString(), EpisodeDetails::class.java)
                offer(episode)
            },
            {
                close(Exception(it.message))
            }
        )

        volleySingleton.addToRequestQueue(jsonObjectRequest)
        awaitClose { jsonObjectRequest.cancel() }
    }
}