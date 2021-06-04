package com.example.androidchallenge.util

import androidx.room.TypeConverter
import com.example.androidchallenge.data.model.SeriesImage

class Converters {
    @TypeConverter
    fun fromSeriesImage(value: SeriesImage?): String? {
        return value?.let { "${value.medium} ${value.original}" }
    }

    @TypeConverter
    fun toSeriesImage(value: String?): SeriesImage? {
        return value?.let { SeriesImage(it.split(" ")[0], it.split(" ")[1]) }
    }
}