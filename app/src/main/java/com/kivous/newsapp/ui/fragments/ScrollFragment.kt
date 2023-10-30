package com.kivous.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.kivous.newsapp.check_network_connectivity.NetworkViewModel
import com.kivous.newsapp.databinding.FragmentScrollBinding
import com.kivous.newsapp.ui.adapters.ScrollNewsAdapter
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import com.kivous.newsapp.utils.Common.gone
import com.kivous.newsapp.utils.Common.visible
import com.kivous.newsapp.utils.Constants.GENERAL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ScrollFragment : Fragment() {
    private var _binding: FragmentScrollBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()
    private val networkViewModel: NetworkViewModel by viewModels()

    @Inject
    lateinit var adapter: ScrollNewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScrollBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        checkDataLoadState()
        binding.viewPager.adapter = adapter
        setDataToAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
                        binding.viewPager.visible()
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

    private fun setDataToAdapter() {
        lifecycleScope.launch {
            viewModel.getScrollNews(GENERAL).distinctUntilChanged().collectLatest {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    private fun whenNoInternet() {
        lifecycleScope.launch {
            networkViewModel.isConnected.collectLatest { isConnected ->
                if (isConnected) {
                    binding.ivWarning.gone()
                    binding.tvNoInternet.gone()
                    adapter.retry()
                } else {
                    binding.ivWarning.visible()
                    binding.tvNoInternet.visible()
                }
            }
        }
    }


}