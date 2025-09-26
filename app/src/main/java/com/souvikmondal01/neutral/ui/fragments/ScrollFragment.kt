package com.souvikmondal01.neutral.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.souvikmondal01.neutral.databinding.FragmentScrollBinding
import com.souvikmondal01.neutral.network.NetworkViewModel
import com.souvikmondal01.neutral.ui.adapters.ScrollNewsAdapter
import com.souvikmondal01.neutral.ui.viewmodels.NewsViewModel
import com.souvikmondal01.neutral.utils.Constants.GENERAL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.collectLatest
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

    @Inject
    lateinit var apiKey: Deferred<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScrollBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeNetworkConnectionAndHandleUI()
        setUpViewPager()
        setDataToAdapter()
        observeAdapterLoadStateAndHandleUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpViewPager() {
        binding.viewPager.adapter = adapter
    }

    private fun setDataToAdapter() {
        lifecycleScope.launch {
            viewModel.getScrollNews(GENERAL, apiKey.await()).collectLatest {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    private fun observeAdapterLoadStateAndHandleUI() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.apply {
                    progressBar.isVisible = loadStates.refresh is LoadState.Loading
                    viewPager.isVisible = loadStates.refresh !is LoadState.Loading
                    cvRefresh.isVisible = loadStates.refresh is LoadState.Error
                }
            }
        }
    }

    private fun observeNetworkConnectionAndHandleUI() {
        lifecycleScope.launch {
            networkViewModel.isConnected.collectLatest { isConnected ->
                binding.ivWarning.isVisible = !isConnected
                binding.tvNoInternet.isVisible = !isConnected
                if (isConnected) {
                    adapter.retry()
                }
            }
        }
    }

}