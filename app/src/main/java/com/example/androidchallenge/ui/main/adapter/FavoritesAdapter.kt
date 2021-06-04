package com.example.androidchallenge.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchallenge.R
import com.example.androidchallenge.data.model.Series
import com.example.androidchallenge.databinding.ItemSeriesBinding
import com.example.androidchallenge.util.listener.SeriesListener

class FavoritesAdapter(
    private var favorites: ArrayList<Series>,
    private val seriesListener: SeriesListener
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

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
        val series = favorites[position]
        holder.binding.isFavorite = true
        holder.binding.series = series
        holder.binding.root.setOnClickListener { seriesListener.onClickListener(series) }
        holder.binding.imgFav.setOnClickListener { seriesListener.onUpdateFavorite(series) }
    }

    override fun getItemCount(): Int = favorites.size

    /**
     * Add or removes a series from the user's favorites.
     */
    fun updateFavorites(list: List<Series>) {
        if (list.isEmpty()) {
            favorites.clear()
            notifyDataSetChanged()
            return
        }
        if (favorites.isEmpty()) {
            favorites.addAll(list)
            notifyDataSetChanged()
            return
        }

        val addFavorites = list.filter { plant ->
            !favorites.map { p -> p.id }.contains(plant.id)
        }

        val removeFavorites = favorites.minus(list)

        removeFavorites.forEach { p ->
            val plant = favorites.find { it.id == p.id }
            notifyItemRemoved(favorites.indexOf(plant))
            favorites.remove(plant)
        }

        addFavorites.forEach { p ->
            favorites.add(p)
            notifyItemInserted(favorites.indexOf(p))
        }
    }

    class ViewHolder(val binding: ItemSeriesBinding) : RecyclerView.ViewHolder(binding.root)
}