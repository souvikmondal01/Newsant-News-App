package com.kivous.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kivous.newsapp.R
import com.kivous.newsapp.check_network_connectivity.NetworkViewModel
import com.kivous.newsapp.data.model.Article
import com.kivous.newsapp.databinding.FragmentSearchBinding
import com.kivous.newsapp.ui.adapters.SearchLoadingStateAdapter
import com.kivous.newsapp.ui.adapters.SearchNewsAdapter
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import com.kivous.newsapp.utils.Common.clearEdittext
import com.kivous.newsapp.utils.Common.gone
import com.kivous.newsapp.utils.Common.showKeyboard
import com.kivous.newsapp.utils.Common.visible
import com.kivous.newsapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()
    private val networkViewModel: NetworkViewModel by viewModels()
    private lateinit var adapter: SearchNewsAdapter

    @Inject
    lateinit var apiKey: Deferred<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SearchNewsAdapter(::onNewsClicked)

        binding.apply {
            showKeyboard(etSearch)
            cvBackArrow.setOnClickListener {
                findNavController().navigateUp()
            }
            etSearch.clearEdittext(cvClear)
        }

        observeNetworkConnectionAndHandleUI()
        setUpRecyclerView()
        setDataToAdapter()
        observeAdapterLoadStateAndHandleUI()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onNewsClicked(article: Article) {
        val bundle = bundleOf(Constants.KEY to Gson().toJson(article))
        findNavController().navigate(R.id.action_searchFragment_to_webViewFragment, bundle)
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = SearchLoadingStateAdapter(), footer = SearchLoadingStateAdapter()
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setDataToAdapter() {
        binding.etSearch.addTextChangedListener { editable ->
            adapter.submitData(lifecycle, PagingData.empty())

            val queryString = editable.toString().trim()

            if (queryString.isNotEmpty()) {
                binding.progressBar.visible()
                lifecycleScope.launch {
                    viewModel.getSearchNews(queryString, apiKey.await()).collectLatest {
                        adapter.submitData(lifecycle, it)
                    }
                }
            } else {
                adapter.submitData(lifecycle, PagingData.empty())
                binding.progressBar.gone()
            }

        }
    }

    private fun observeAdapterLoadStateAndHandleUI() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->

                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        binding.progressBar.visible()
                    }

                    is LoadState.NotLoading -> {
                        binding.progressBar.gone()
                        binding.recyclerView.visible()
                    }

                    is LoadState.Error -> {
                        binding.progressBar.gone()
                        binding.recyclerView.gone()
                    }

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