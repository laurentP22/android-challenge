package com.example.androidchallenge.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Series(
    @PrimaryKey val id: Int,
    val name: String,
    val image: SeriesImage?
)