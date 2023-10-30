package com.kivous.newsapp.ui.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.kivous.newsapp.R
import com.kivous.newsapp.check_network_connectivity.NetworkViewModel
import com.kivous.newsapp.databinding.FragmentBusinessBinding
import com.kivous.newsapp.ui.adapters.LoadingStateAdapter
import com.kivous.newsapp.ui.adapters.NewsAdapter
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import com.kivous.newsapp.utils.Common.gone
import com.kivous.newsapp.utils.Common.visible
import com.kivous.newsapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScienceFragment : Fragment() {
    private var _binding: FragmentBusinessBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()
    private val networkViewModel: NetworkViewModel by viewModels()
    lateinit var adapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NewsAdapter(this, viewModel, R.id.action_categoryFragment_to_webViewFragment)
        lifecycleScope.launch {
            networkViewModel.isConnected.collectLatest { isConnected ->
                binding.cvRefresh.setOnClickListener {
                    if (isConnected) {
                        adapter.refresh()
                    }
                }
            }
        }
        whenNoInternet()
        setUpRecyclerView()
        checkDataLoadState()
        setDataToAdapter()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadingStateAdapter(),
            footer = LoadingStateAdapter()
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setDataToAdapter() {
        lifecycleScope.launch {
            viewModel.getCategoryNews(Constants.SCIENCE).distinctUntilChanged().collectLatest {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    private fun checkDataLoadState() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        binding.progressBar.visible()
                        binding.cvRefresh.gone()
                    }

                    is LoadState.NotLoading -> {
                        binding.progressBar.gone()
                        binding.recyclerView.visible()
                        binding.cvRefresh.gone()
                    }

                    is LoadState.Error -> {
                        binding.progressBar.gone()
                        binding.cvRefresh.visible()
                    }
                }
            }
        }
    }

    private fun whenNoInternet() {
        lifecycleScope.launch {
            networkViewModel.isConnected.collectLatest { isConnected ->
                if (isConnected) {
                    binding.viewNetworkError.gone()
                    adapter.retry()
                } else {
                    binding.viewNetworkError.visible()
                }
            }
        }
    }

}