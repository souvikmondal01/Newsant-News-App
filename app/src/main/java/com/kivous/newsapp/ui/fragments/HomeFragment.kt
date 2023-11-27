package com.kivous.newsapp.ui.fragments


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
import com.kivous.newsapp.databinding.FragmentHomeBinding
import com.kivous.newsapp.ui.adapters.LoadingStateAdapter
import com.kivous.newsapp.ui.adapters.NewsAdapter
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import com.kivous.newsapp.utils.Common.gone
import com.kivous.newsapp.utils.Common.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()
    private val networkViewModel: NetworkViewModel by viewModels()
    lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NewsAdapter(
            this, viewModel, R.id.action_homeFragment_to_webViewFragment
        )
        binding.cvBackArrow.setOnClickListener {
            requireActivity().finish()
        }

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
            header = LoadingStateAdapter(), footer = LoadingStateAdapter()
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setDataToAdapter() {
        lifecycleScope.launch {
            viewModel.apiKey.collectLatest { apiKey ->
                viewModel.getBreakingNews(apiKey).distinctUntilChanged().collectLatest {
                    adapter.submitData(lifecycle, it)
                }
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

