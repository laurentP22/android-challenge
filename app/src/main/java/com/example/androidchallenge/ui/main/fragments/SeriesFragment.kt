package com.example.androidchallenge.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.androidchallenge.R
import com.example.androidchallenge.data.model.Series
import com.example.androidchallenge.data.response.ResultObserver
import com.example.androidchallenge.data.response.Status
import com.example.androidchallenge.databinding.FragmentSeriesBinding
import com.example.androidchallenge.ui.main.adapter.SeriesAdapter
import com.example.androidchallenge.ui.main.viewmodel.MainViewModel
import com.example.androidchallenge.ui.main.viewmodel.SeriesViewModel
import com.example.androidchallenge.util.BaseFragment
import com.example.androidchallenge.util.SpaceItemDecoration
import com.example.androidchallenge.util.listener.SeriesListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SeriesFragment : BaseFragment(), SeriesListener {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: SeriesViewModel by activityViewModels()
    private lateinit var binding: FragmentSeriesBinding
    private lateinit var seriesAdapter: SeriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        seriesAdapter = SeriesAdapter(
            mainViewModel.favorites.value ?: listOf(),
            viewModel.series.value?.data ?: listOf(),
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_series, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.apply {
            setOnRefreshListener {
                seriesAdapter.reset()
                viewModel.loadSeries()
            }
        }

        binding.rvSeries.apply {
            adapter = seriesAdapter
            addItemDecoration(SpaceItemDecoration(16))
            viewModel.scrollingState?.let { layoutManager?.onRestoreInstanceState(it) }

            setHasFixedSize(true)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val isBottomReached = !recyclerView.canScrollVertically(1)
                    if (isBottomReached && seriesAdapter.itemCount > 0) {
                        viewModel.loadNextPage()
                    }
                }
            })
        }

        mainViewModel.favorites.observe(viewLifecycleOwner, {
            it?.apply { seriesAdapter.updateFavorites(ArrayList(this)) }
        })

        viewModel.series.observe(viewLifecycleOwner, ResultObserver {
            when (it.status) {
                Status.LOADING -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                Status.ERROR -> {
                    seriesAdapter.reset()
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                else -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    it.data?.apply { seriesAdapter.addAll(this) }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.scrollingState = binding.rvSeries.layoutManager?.onSaveInstanceState()
    }

    override fun onClickListener(series: Series) {
        val action = SeriesFragmentDirections.openDetails(sid = series.id)
        navController.navigate(action)
    }

    override fun onUpdateFavorite(series: Series) {
        mainViewModel.updateFavorite(series)
    }
}