package com.example.androidchallenge.util.listener

import com.example.androidchallenge.data.model.Episode

interface EpisodeListener {
    fun onClickListener(episode: Episode)
}