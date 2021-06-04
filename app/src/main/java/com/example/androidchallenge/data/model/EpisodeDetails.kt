package com.example.androidchallenge.data.model

data class EpisodeDetails(
    val id: Int,
    val name: String,
    val summary: String,
    val image: SeriesImage?,
    val season: Int,
    val number: Int
)