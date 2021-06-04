package com.example.androidchallenge.ui.series.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchallenge.R
import com.example.androidchallenge.data.model.Season
import com.example.androidchallenge.databinding.ItemSeasonBinding
import com.example.androidchallenge.util.SpaceItemDecoration
import com.example.androidchallenge.util.listener.EpisodeListener

class SeasonsAdapter(
    private var seasons: List<Season> = listOf(),
    private var episodeListener: EpisodeListener
) : RecyclerView.Adapter<SeasonsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemSeasonBinding>(
            layoutInflater,
            R.layout.item_season,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episode = seasons[position]

        holder.binding.season = episode
        holder.binding.rvEpisodes.apply {
            adapter = EpisodesAdapter(episode.episodes, episodeListener)
            addItemDecoration(SpaceItemDecoration(16))
            setHasFixedSize(true)
        }
    }

    override fun getItemCount(): Int = seasons.size

    /**
     * Add series to the dataset.
     */
    fun update(seasons: List<Season>) {
        this.seasons = seasons
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemSeasonBinding) : RecyclerView.ViewHolder(binding.root)
}