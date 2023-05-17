package com.kivous.newsapp.ui.fragments.categories

import android.content.Intent
import android.os.Bundle
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
import com.kivous.newsapp.adapters.CategoryNewsAdapter
import com.kivous.newsapp.adapters.CategoryNewsListener
import com.kivous.newsapp.common.Constants
import com.kivous.newsapp.common.Utils.gone
import com.kivous.newsapp.common.Utils.invisible
import com.kivous.newsapp.common.Utils.visible
import com.kivous.newsapp.databinding.FragmentBusinessBinding
import com.kivous.newsapp.model.Article
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScienceFragment : Fragment(), CategoryNewsListener {
    private var _binding: FragmentBusinessBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()
    lateinit var adapter: CategoryNewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBusinessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = viewModel.categoryNews("science")
        data.observe(viewLifecycleOwner) { response ->
            adapter.submitData(lifecycle, response)
        }
        adapter = CategoryNewsAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
    }
    override fun onStart() {
        super.onStart()
        if (!viewModel.hasInternetConnection()) {
            binding.pb.invisible()
            binding.tv.visible()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun handleListView(holder: CategoryNewsAdapter.ViewHolder, article: Article?) {
        holder.apply {
            binding.apply {
                var isSaved = false
                ivSave.setOnClickListener { view ->
                    if (!isSaved) {
                        view.setBackgroundResource(R.drawable.bookmark)
                        article?.let { viewModel.saveArticle(it) }
                        Snackbar.make(
                            requireView(),
                            "Article saved successfully",
                            Snackbar.LENGTH_SHORT
                        ).apply {
                            setAction("Show") {
                                findNavController().navigate(R.id.action_categoryFragment_to_favouriteFragment)
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
                    shareArticle(article?.url.toString())
                }
            }

            itemView.setOnClickListener {
                val bundle = bundleOf(Constants.KEY to article?.url)
                findNavController().navigate(
                    R.id.action_categoryFragment_to_articleFragment, bundle
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