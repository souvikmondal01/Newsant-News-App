package com.kivous.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kivous.newsapp.adapters.ScrollNewsAdapter
import com.kivous.newsapp.common.Utils.hideKeyboard
import com.kivous.newsapp.common.Utils.invisible
import com.kivous.newsapp.common.Utils.toast
import com.kivous.newsapp.databinding.FragmentScrollBinding
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ScrollFragment : Fragment() {
    private var _binding: FragmentScrollBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: ScrollNewsAdapter
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

        adapter = ScrollNewsAdapter()
        val data = viewModel.scrollNews()
        data.observe(viewLifecycleOwner) { response ->
            adapter.submitData(lifecycle, response)
            binding.viewPager.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        hideKeyboard()
        if (!viewModel.hasInternetConnection()) {
            binding.pb.invisible()
            toast("No internet connection")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}