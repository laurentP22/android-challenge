package com.example.androidchallenge.util.listener

import com.example.androidchallenge.data.model.Series

interface SeriesListener {
    fun onClickListener(series: Series)
    fun onUpdateFavorite(series: Series)
}