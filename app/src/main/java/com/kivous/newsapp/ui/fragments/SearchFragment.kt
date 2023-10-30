package com.kivous.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
import com.kivous.newsapp.utils.Common.hideKeyboard
import com.kivous.newsapp.utils.Common.showKeyboard
import com.kivous.newsapp.utils.Common.visible
import com.kivous.newsapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()
    private val networkViewModel: NetworkViewModel by viewModels()
    private lateinit var adapter: SearchNewsAdapter
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
            clearEdittext(etSearch, cvClear)
        }

        setUpRecyclerView()
        checkDataLoadState()
        whenNoInternet()

        binding.etSearch.addTextChangedListener { editable ->
            adapter.submitData(lifecycle, PagingData.empty())
            editable?.let {
                if (editable.toString().isNotEmpty()) {
                    lifecycleScope.launch {
                        viewModel.getSearchNews(editable.trim().toString()).distinctUntilChanged()
                            .collectLatest {
                                adapter.submitData(lifecycle, it)
                            }
                    }
                } else {
                    adapter.submitData(lifecycle, PagingData.empty()).also {
                        binding.progressBar.gone()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onNewsClicked(article: Article) {
        val bundle = bundleOf(Constants.KEY to Gson().toJson(article))
        findNavController().navigate(
            R.id.action_searchFragment_to_webViewFragment, bundle
        )
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = SearchLoadingStateAdapter(), footer = SearchLoadingStateAdapter()
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun checkDataLoadState() {
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