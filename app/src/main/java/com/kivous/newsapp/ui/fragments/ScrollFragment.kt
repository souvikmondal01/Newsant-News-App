package com.kivous.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kivous.newsapp.adapters.ScrollNewsAdapter
import com.kivous.newsapp.common.Resource
import com.kivous.newsapp.common.Utils.gone
import com.kivous.newsapp.common.Utils.hideKeyboard
import com.kivous.newsapp.common.Utils.toast
import com.kivous.newsapp.common.Utils.visible
import com.kivous.newsapp.databinding.FragmentScrollBinding
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ScrollFragment : Fragment() {
    private var _binding: FragmentScrollBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ScrollNewsAdapter
    private val viewModel: NewsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScrollBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBreakingNews("us")
        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.pb.gone()
                    response.data?.let { newsResponse ->
                        adapter = ScrollNewsAdapter(newsResponse.articles)
                        binding.viewPager.adapter = adapter
                    }
                }

                is Resource.Error -> {
                    binding.pb.gone()
                    response.message?.let {
                        toast(it)
                    }
                }

                is Resource.Loading -> {
                    binding.pb.visible()
                }

            }

        }


    }

    override fun onStart() {
        super.onStart()
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}