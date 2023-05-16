package com.kivous.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kivous.newsapp.common.Utils.hideKeyboard
import com.kivous.newsapp.databinding.FragmentAccountBinding
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()
//    lateinit var adapter: CategoryNewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.categoryNews.observe(viewLifecycleOwner) { response ->
//            Log.d("SSS", response.toString())
//            adapter.submitData(lifecycle, response)
//        }
//
//        adapter = CategoryNewsAdapter()
//        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.recyclerView.setHasFixedSize(true)
//        binding.recyclerView.adapter = adapter
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