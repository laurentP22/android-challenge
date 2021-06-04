package com.example.androidchallenge.data.model

data class Episode(
    val id: Int,
    val name: String,
    val image: SeriesImage?,
    val season: Int,
    val number: Int,
    val airdate: String,
    val runtime: Int,
)