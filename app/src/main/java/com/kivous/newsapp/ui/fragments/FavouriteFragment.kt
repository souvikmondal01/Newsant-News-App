package com.kivous.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kivous.newsapp.R
import com.kivous.newsapp.adapters.SearchNewsAdapter
import com.kivous.newsapp.adapters.SearchNewsListener
import com.kivous.newsapp.common.Constants
import com.kivous.newsapp.common.Utils.hideKeyboard
import com.kivous.newsapp.databinding.FragmentFavouriteBinding
import com.kivous.newsapp.model.Article
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : Fragment(), SearchNewsListener {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchNewsAdapter
    private val viewModel: NewsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        adapter = SearchNewsAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.cvBackArrow.setOnClickListener {
            activity?.onBackPressed()
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

    override fun handleListView(holder: SearchNewsAdapter.ViewHolder, article: Article) {
        holder.apply {
            itemView.setOnClickListener {
                val bundle = bundleOf(Constants.KEY to article.url)
                findNavController().navigate(
                    R.id.action_favouriteFragment_to_articleFragment, bundle
                )
            }

            binding.apply {
                ivSave.setBackgroundResource(R.drawable.bookmark)
                ivSave.setOnClickListener {
                }
            }
        }
    }

}