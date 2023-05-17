package com.kivous.newsapp.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.kivous.newsapp.R
import com.kivous.newsapp.adapters.NewsAdapter
import com.kivous.newsapp.adapters.NewsListener
import com.kivous.newsapp.common.Constants.KEY
import com.kivous.newsapp.common.Constants.QUERY_PAGE_SIZE
import com.kivous.newsapp.common.Resource
import com.kivous.newsapp.common.Utils.gone
import com.kivous.newsapp.common.Utils.hideKeyboard
import com.kivous.newsapp.common.Utils.toast
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

        viewModel.getBreakingNews("in")
        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {

                    binding.pb.gone()
                    isLoading = false
                    response.data?.let { newsResponse ->
                        adapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
//                        if (isLastPage) {
//                            binding.recyclerView.setPadding(0, 0, 0, 0)
//                        }
                    }
                }

                is Resource.Error -> {
                    binding.pb.gone()
                    isLoading = false
                    response.message?.let {
                        toast(it)
                    }
                }

                is Resource.Loading -> {
                    binding.pb.visible()
                    isLoading = true
                }
            }
        }


        adapter = NewsAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addOnScrollListener(this@HomeFragment.scrollListener)

    }

    override fun onStart() {
        super.onStart()
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visible()
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getBreakingNews("in")
                isScrolling = false
            }
        }
    }

    override fun handleListView(holder: NewsAdapter.ViewHolder, article: Article) {
        holder.apply {
            binding.apply {
                var isSaved = false
                ivSave.setOnClickListener { view ->
                    if (!isSaved) {
                        view.setBackgroundResource(R.drawable.bookmark)
                        viewModel.saveArticle(article)
                        Snackbar.make(
                            requireView(),
                            "Article saved successfully",
                            Snackbar.LENGTH_SHORT
                        ).apply {
                            setAction("Show") {
                                findNavController().navigate(R.id.action_homeFragment_to_favouriteFragment)
                                val navBar =
                                    requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
                                navBar.gone()
                            }
                            show()
                        }
                    } else {
                        view.setBackgroundResource(R.drawable.bookmark_border)
                    }
                    isSaved = !isSaved
                }

                ivShare.setOnClickListener {
                    shareArticle(article.url.toString())
                }
            }

            itemView.setOnClickListener {
                val bundle = bundleOf(KEY to article.url)
                findNavController().navigate(
                    R.id.action_homeFragment_to_articleFragment, bundle
                )
            }
        }
    }

    private fun shareArticle(link: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, link)
        val chooser = Intent.createChooser(intent, "")
        activity?.startActivity(chooser)
    }


}

