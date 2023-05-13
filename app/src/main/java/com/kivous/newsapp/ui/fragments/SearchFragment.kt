package com.kivous.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kivous.newsapp.R
import com.kivous.newsapp.adapters.NewsAdapter
import com.kivous.newsapp.adapters.NewsListener
import com.kivous.newsapp.common.Constants
import com.kivous.newsapp.common.Resource
import com.kivous.newsapp.common.Utils.gone
import com.kivous.newsapp.common.Utils.invisible
import com.kivous.newsapp.common.Utils.visible
import com.kivous.newsapp.databinding.FragmentSearchBinding
import com.kivous.newsapp.db.ArticleDatabase
import com.kivous.newsapp.model.Article
import com.kivous.newsapp.repositories.NewsRepository
import com.kivous.newsapp.ui.activities.MainActivity
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import com.kivous.newsapp.ui.viewmodels.NewsViewModelProviderFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), NewsListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etSearch.requestFocus()
        val newsRepository = NewsRepository(ArticleDatabase(requireContext()))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchForNews(editable.toString())
                    }
                }
            }
        }


        viewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.pb.gone()
                    response.data?.let { newsResponse ->
                        adapter.differ.submitList(newsResponse.articles)
                    }
                }

                is Resource.Error -> {
                    binding.pb.gone()
                    response.message?.let {
                        Log.d("ERR", "An error occurred: $it")
                    }
                }

                is Resource.Loading -> {
                    binding.pb.visible()
                }
            }
        }

        adapter = NewsAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onArticleClick(holder: NewsAdapter.ViewHolder, article: Article) {
//        val bundle = Bundle().apply {
//            putSerializable(Constants.KEY, article)
//        }
//        findNavController().navigate(
//            R.id.action_searchFragment_to_articleFragment, bundle
//        )
    }

    override fun onSaveClick(article: Article) {
    }

}