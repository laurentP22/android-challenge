package com.example.androidchallenge.ui.series.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.androidchallenge.R
import com.example.androidchallenge.data.model.Episode
import com.example.androidchallenge.databinding.ItemEpisodeBinding
import com.example.androidchallenge.util.listener.EpisodeListener

class EpisodesAdapter(
    private var episodes: List<Episode> = listOf(),
    private var episodeListener: EpisodeListener
) : RecyclerView.Adapter<EpisodesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemEpisodeBinding>(
            layoutInflater,
            R.layout.item_episode,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episode = episodes[position]
        holder.binding.episode = episode
        holder.binding.root.setOnClickListener { episodeListener.onClickListener(episode) }

        episode.image?.apply {
            Glide.with(holder.binding.imgEpisode)
                .load(original)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.color.design_default_color_secondary)
                .into(holder.binding.imgEpisode)
        }
    }

    override fun getItemCount(): Int = episodes.size

    class ViewHolder(val binding: ItemEpisodeBinding) : RecyclerView.ViewHolder(binding.root)
}