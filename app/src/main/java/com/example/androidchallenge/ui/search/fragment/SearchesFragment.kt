package com.example.androidchallenge.ui.search.fragment


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.androidchallenge.R
import com.example.androidchallenge.data.model.Series
import com.example.androidchallenge.data.response.ResultObserver
import com.example.androidchallenge.data.response.Status
import com.example.androidchallenge.databinding.FragmentSearchesBinding
import com.example.androidchallenge.ui.main.adapter.SeriesAdapter
import com.example.androidchallenge.ui.main.viewmodel.MainViewModel
import com.example.androidchallenge.ui.search.viewmodel.SearchesViewModel
import com.example.androidchallenge.util.BaseFragment
import com.example.androidchallenge.util.SpaceItemDecoration
import com.example.androidchallenge.util.listener.SeriesListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchesFragment : BaseFragment(), SeriesListener {
    private val mainViewModel: MainViewModel by viewModels()
    private val viewModel: SearchesViewModel by viewModels()
    private lateinit var binding: FragmentSearchesBinding
    private lateinit var seriesAdapter: SeriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        seriesAdapter = SeriesAdapter(
            mainViewModel.favorites.value ?: listOf(),
            viewModel.series.value?.data ?: listOf(), this
        )
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_searches, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefreshLayout.apply {
            setOnRefreshListener {
                viewModel.searchSeries()
            }
        }

        binding.rvSeries.apply {
            adapter = seriesAdapter
            addItemDecoration(SpaceItemDecoration(16))
            setHasFixedSize(true)
        }

        mainViewModel.favorites.observe(viewLifecycleOwner, {
            it?.apply { seriesAdapter.updateFavorites(ArrayList(this)) }
        })

        viewModel.series.observe(viewLifecycleOwner, ResultObserver {
            when (it.status) {
                Status.LOADING -> {
                    seriesAdapter.reset()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onClickListener(series: Series) {
        val action = SearchesFragmentDirections.openDetails(sid = series.id)
        navController.navigate(action)
    }

    override fun onUpdateFavorite(series: Series) {
        mainViewModel.updateFavorite(series)
    }
}