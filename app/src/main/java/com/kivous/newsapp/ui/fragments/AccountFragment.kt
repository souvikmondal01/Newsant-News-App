package com.kivous.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kivous.newsapp.adapters.CategoryNewsAdapter
import com.kivous.newsapp.common.Resource
import com.kivous.newsapp.common.Utils.hideKeyboard
import com.kivous.newsapp.common.Utils.toast
import com.kivous.newsapp.databinding.FragmentAccountBinding
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()
    lateinit var adapter: CategoryNewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.categoryNews.observe(viewLifecycleOwner) { response ->
            Log.d("SSS", response.toString())
            adapter.submitData(lifecycle, response)
        }

        adapter = CategoryNewsAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter


        binding.one.setOnClickListener {
            viewModel.categoryNews.observe(viewLifecycleOwner) { response ->
                Log.d("SSS", response.toString())
                adapter.submitData(lifecycle, response)
            }
        }


        binding.two.setOnClickListener {
            viewModel.scienceNews.observe(viewLifecycleOwner) { response ->
                Log.d("SSS", response.toString())
                adapter.submitData(lifecycle, response)
            }
        }

        binding.three.setOnClickListener {
            viewModel.eData("entertainment")
            getData()
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


    fun getData(){
        viewModel.e.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { data ->
                        data.observe(viewLifecycleOwner) { res ->
                            adapter.submitData(lifecycle, res)
                        }
                    }
                }

                is Resource.Error -> {
                    response.message?.let {
                        toast(it)
                    }
                }

                is Resource.Loading -> {
                    toast("Loading")
                }
            }

        }
    }
}