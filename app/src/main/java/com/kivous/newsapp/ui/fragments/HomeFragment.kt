package com.kivous.newsapp.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.kivous.newsapp.R
import com.kivous.newsapp.adapters.NewsAdapter
import com.kivous.newsapp.adapters.NewsListener
import com.kivous.newsapp.common.Constants.KEY
import com.kivous.newsapp.common.Resource
import com.kivous.newsapp.common.Utils.gone
import com.kivous.newsapp.common.Utils.visible
import com.kivous.newsapp.databinding.FragmentHomeBinding
import com.kivous.newsapp.model.Article
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), NewsListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NewsAdapter
    private val viewModel: NewsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cvBackArrow.setOnClickListener {
            activity?.finish()
        }

        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
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

    override fun onStart() {
        super.onStart()
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onArticleClick(holder: NewsAdapter.ViewHolder, article: Article) {
        val bundle = bundleOf(KEY to article.url)
        findNavController().navigate(
            R.id.action_homeFragment_to_articleFragment, bundle
        )
    }

    override fun onSaveClick(article: Article) {
        viewModel.saveArticle(article)
        Snackbar.make(requireView(), "Article saved successfully", Snackbar.LENGTH_SHORT).apply {
            setAction("Show") {
                findNavController().navigate(R.id.action_homeFragment_to_favouriteFragment)
                val navBar =
                    requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
                navBar.gone()
            }
            show()
        }

    }


}