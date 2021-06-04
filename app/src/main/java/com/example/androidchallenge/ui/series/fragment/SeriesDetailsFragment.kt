package com.example.androidchallenge.ui.series.fragment

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.androidchallenge.R
import com.example.androidchallenge.data.model.Episode
import com.example.androidchallenge.data.response.ResultObserver
import com.example.androidchallenge.data.response.Status
import com.example.androidchallenge.databinding.FragmentSeriesDetailsBinding
import com.example.androidchallenge.ui.series.adapter.SeasonsAdapter
import com.example.androidchallenge.ui.series.viewmodel.SeriesDetailsViewModel
import com.example.androidchallenge.util.BaseFragment
import com.example.androidchallenge.util.SpaceItemDecoration
import com.example.androidchallenge.util.listener.EpisodeListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeriesDetailsFragment : BaseFragment(), EpisodeListener {
    private val viewModel: SeriesDetailsViewModel by viewModels()
    private lateinit var binding: FragmentSeriesDetailsBinding
    private lateinit var seasonsAdapter: SeasonsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        seasonsAdapter = SeasonsAdapter(viewModel.seasons.value?.data ?: listOf(), this)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_series_details, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSeasons.apply {
            adapter = seasonsAdapter
            addItemDecoration(SpaceItemDecoration(16))
            setHasFixedSize(true)
        }

        viewModel.seasons.observe(viewLifecycleOwner, ResultObserver {
            if (it.status == Status.SUCCESS) {
                it.data?.apply {
                    seasonsAdapter.update(this)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onClickListener(episode: Episode) {
        val action = SeriesDetailsFragmentDirections.openDetails(eid = episode.id)
        navController.navigate(action)
    }
}