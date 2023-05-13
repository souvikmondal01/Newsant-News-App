package com.kivous.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kivous.newsapp.R
import com.kivous.newsapp.adapters.NewsAdapter
import com.kivous.newsapp.adapters.NewsListener
import com.kivous.newsapp.common.Resource
import com.kivous.newsapp.common.Utils.gone
import com.kivous.newsapp.common.Utils.visible
import com.kivous.newsapp.databinding.FragmentFavouriteBinding
import com.kivous.newsapp.db.ArticleDatabase
import com.kivous.newsapp.model.Article
import com.kivous.newsapp.repositories.NewsRepository
import com.kivous.newsapp.ui.activities.MainActivity
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import com.kivous.newsapp.ui.viewmodels.NewsViewModelProviderFactory

class FavouriteFragment : Fragment(), NewsListener {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newsRepository = NewsRepository(ArticleDatabase(requireContext()))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = adapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerView)
        }


        viewModel.getSavedNews().observe(viewLifecycleOwner) { articles ->

            adapter.differ.submitList(articles)

        }

        adapter = NewsAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.cvBackArrow.setOnClickListener {
            activity?.onBackPressed()
        }

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