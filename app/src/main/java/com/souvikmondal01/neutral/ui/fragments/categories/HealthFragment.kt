package com.souvikmondal01.neutral.ui.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.souvikmondal01.neutral.R
import com.souvikmondal01.neutral.network.NetworkViewModel
import com.souvikmondal01.neutral.databinding.FragmentBusinessBinding
import com.souvikmondal01.neutral.ui.adapters.LoadingStateAdapter
import com.souvikmondal01.neutral.ui.adapters.NewsAdapter
import com.souvikmondal01.neutral.ui.viewmodels.NewsViewModel
import com.souvikmondal01.neutral.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HealthFragment : Fragment() {
    private var _binding: FragmentBusinessBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()
    private val networkViewModel: NetworkViewModel by viewModels()
    lateinit var adapter: NewsAdapter

    @Inject
    lateinit var apiKey: Deferred<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NewsAdapter(this, viewModel, R.id.action_categoryFragment_to_webViewFragment)

        observeNetworkConnectionAndHandleUI()
        setUpRecyclerView()
        setDataToAdapter()
        observeAdapterLoadStateAndHandleUI()

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
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getCategoryNews(Constants.HEALTH, apiKey.await())
                .collectLatest {
                    adapter.submitData(lifecycle, it)
                }
        }
    }

    private fun observeAdapterLoadStateAndHandleUI() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.apply {
                    progressBar.isVisible = loadStates.refresh is LoadState.Loading
                    recyclerView.isVisible = loadStates.refresh !is LoadState.Loading
                    cvRefresh.isVisible = loadStates.refresh is LoadState.Error
                }
            }
        }
    }

    private fun observeNetworkConnectionAndHandleUI() {
        lifecycleScope.launch {
            networkViewModel.isConnected.collectLatest { isConnected ->
                binding.viewNetworkError.isVisible = !isConnected
                if (isConnected) {
                    adapter.retry()
                }
            }
        }
    }

}