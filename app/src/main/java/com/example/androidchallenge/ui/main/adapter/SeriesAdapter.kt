package com.example.androidchallenge.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchallenge.R
import com.example.androidchallenge.data.model.Series
import com.example.androidchallenge.databinding.ItemSeriesBinding
import com.example.androidchallenge.util.listener.SeriesListener
import java.util.*

class SeriesAdapter(
    private var favorites: List<Series>,
    private var series: List<Series> = listOf(),
    private val seriesListener: SeriesListener
) : RecyclerView.Adapter<SeriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemSeriesBinding>(
            layoutInflater,
            R.layout.item_series,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val series = series[position]
        holder.binding.isFavorite = favorites.contains(series)
        holder.binding.series = series
        holder.binding.root.setOnClickListener { seriesListener.onClickListener(series) }
        holder.binding.imgFav.setOnClickListener { seriesListener.onUpdateFavorite(series) }
    }

    override fun getItemCount(): Int = series.size

    /**
     * Add series to the dataset.
     */
    fun addAll(newSeries: List<Series>) {
        val positionStart = series.size
        series = newSeries
        notifyItemRangeInserted(positionStart, newSeries.size - positionStart)
    }

    /**
     * Reset the dataset.
     */
    fun reset() {
        series = listOf()
        notifyDataSetChanged()
    }

    /**
     * Add or removes a series from the user's favorites.
     */
    fun updateFavorites(newFavorites: ArrayList<Series>) {
        if (favorites != newFavorites) {
            val seriesToUpdate = arrayListOf<Series>().apply {
                addAll(newFavorites.filter { fav -> !favorites.contains(fav) })
                addAll(favorites.filter { fav -> !newFavorites.contains(fav) })
            }
            series.filter { doc -> seriesToUpdate.contains(doc) }.forEach { doc ->
                notifyItemChanged(series.indexOf(doc), Unit)
            }
            favorites = newFavorites
        }
    }

    class ViewHolder(val binding: ItemSeriesBinding) : RecyclerView.ViewHolder(binding.root)
}