package com.kivous.newsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kivous.newsapp.adapters.NewsAdapter
import com.kivous.newsapp.adapters.NewsListener
import com.kivous.newsapp.common.Resource
import com.kivous.newsapp.common.Utils
import com.kivous.newsapp.common.Utils.gone
import com.kivous.newsapp.common.Utils.visible
import com.kivous.newsapp.databinding.FragmentSearchBinding
import com.kivous.newsapp.model.Article
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment(), NewsListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var adapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val imgr = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            etSearch.requestFocus()
            cvBackArrow.setOnClickListener {
                activity?.onBackPressed()
            }
            Utils.clearEdittext(etSearch, cvClear)
        }


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

    }

    override fun onSaveClick(article: Article) {
    }

}