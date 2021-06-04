package com.example.androidchallenge.data.model

data class SeriesDetails(
    val id: Int,
    val name: String,
    val image: SeriesImage?,
    val genres: List<String>,
    val summary: String,
)