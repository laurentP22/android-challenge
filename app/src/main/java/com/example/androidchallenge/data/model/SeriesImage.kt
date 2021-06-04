package com.example.androidchallenge.data.model

import androidx.room.Entity

@Entity
data class SeriesImage(
    val medium: String,
    val original: String,
)