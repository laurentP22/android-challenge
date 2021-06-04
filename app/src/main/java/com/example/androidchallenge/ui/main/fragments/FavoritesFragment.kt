package com.example.androidchallenge.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.androidchallenge.R
import com.example.androidchallenge.data.model.Series
import com.example.androidchallenge.databinding.FragmentFavoritesBinding
import com.example.androidchallenge.ui.main.adapter.FavoritesAdapter
import com.example.androidchallenge.ui.main.viewmodel.MainViewModel
import com.example.androidchallenge.util.BaseFragment
import com.example.androidchallenge.util.SpaceItemDecoration
import com.example.androidchallenge.util.listener.SeriesListener

class FavoritesFragment : BaseFragment(), SeriesListener {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoritesAdapter = FavoritesAdapter(
            mainViewModel.favorites.value ?: arrayListOf(),
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_favorites, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = mainViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSeries.apply {
            adapter = favoritesAdapter
            addItemDecoration(SpaceItemDecoration(16))
            setHasFixedSize(true)
        }

        mainViewModel.favorites.observe(viewLifecycleOwner, {
            it?.apply { favoritesAdapter.updateFavorites(this) }
        })
    }

    override fun onClickListener(series: Series) {
        val action = FavoritesFragmentDirections.openDetails(series.id)
        navController.navigate(action)
    }

    override fun onUpdateFavorite(series: Series) {
        mainViewModel.updateFavorite(series)
    }
}