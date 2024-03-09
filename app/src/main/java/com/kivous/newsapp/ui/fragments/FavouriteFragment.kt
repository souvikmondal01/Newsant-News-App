package com.kivous.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.kivous.newsapp.R
import com.kivous.newsapp.data.model.Article
import com.kivous.newsapp.databinding.FragmentFavouriteBinding
import com.kivous.newsapp.ui.adapters.FavouriteNewsAdapter
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import com.kivous.newsapp.utils.Constants.KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouriteFragment : Fragment() {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavouriteNewsAdapter
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var bottomNav: BottomNavigationView
    private var snackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNav = requireActivity().findViewById(R.id.bottom_navigation)

        adapter = FavouriteNewsAdapter(::onNewsClicked, ::onBookmarkClicked)

        binding.cvBackArrow.setOnClickListener {
            findNavController().navigateUp()
        }

        setUpRecyclerView()
        setDataToAdapter()
        onSwipeDeleteArticle()
        observeArticleIsEmptyOrNotAndUpdateUI()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onNewsClicked(article: Article) {
        val bundle = bundleOf(KEY to Gson().toJson(article))
        findNavController().navigate(
            R.id.action_favouriteFragment_to_webViewFragment, bundle
        )
    }

    private fun onBookmarkClicked(article: Article) {
        deleteArticle(article)
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setDataToAdapter() {
        lifecycleScope.launch {
            viewModel.getSavedNews().collectLatest { articles ->
                adapter.differ.submitList(articles)
            }
        }
    }

    private fun onSwipeDeleteArticle() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.START or ItemTouchHelper.END
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                val article = adapter.differ.currentList[position]
                deleteArticle(article)
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerView)

    }

    private fun observeArticleIsEmptyOrNotAndUpdateUI() {
        lifecycleScope.launch {
            viewModel.isArticleListEmpty().collectLatest {
                binding.apply {
                    tvEmpty.isVisible = it
                    recyclerView.isVisible = !it
                }
            }
        }
    }

    private fun deleteArticle(article: Article) {
        viewModel.deleteArticle(article)
        snackBar =
            Snackbar.make(requireView(), "Article deleted successfully", Snackbar.LENGTH_LONG)
                .setAnchorView(bottomNav).setAction("Undo") {
                    viewModel.insertArticle(article)
                }
        snackBar?.show()
    }

    override fun onDetach() {
        super.onDetach()
        snackBar?.takeIf { it.isShown }?.dismiss()
    }

}