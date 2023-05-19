package com.kivous.newsapp.ui.fragments.categories

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
import com.kivous.newsapp.common.Utils.shareArticle
import com.kivous.newsapp.common.Utils.visible
import com.kivous.newsapp.databinding.FragmentBusinessBinding
import com.kivous.newsapp.model.Article
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HealthFragment : Fragment(), CategoryNewsListener {
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
        val data = viewModel.categoryNews("health")
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
                GlobalScope.launch {
                    val data = viewModel.isExist(article?.url.toString())
                    withContext(Dispatchers.Main) {
                        var check = true
                        if (data != 0) { // means data already exists
                            ivSave.setImageResource(R.drawable.bookmark)
                            ivSave.setOnClickListener {

                                Snackbar.make(
                                    requireView(),
                                    "Article already saved",
                                    Snackbar.LENGTH_SHORT
                                ).apply {
                                    setAction("Ok") {
                                        dismiss()
                                    }
                                    show()
                                }
                            }
                        } else {
                            ivSave.setImageResource(R.drawable.bookmark_border)
                            ivSave.setOnClickListener {
                                if (!check) {
                                    Snackbar.make(
                                        requireView(),
                                        "Article already saved",
                                        Snackbar.LENGTH_SHORT
                                    ).apply {
                                        setAction("Ok") {
                                            dismiss()
                                        }
                                        show()
                                    }

                                } else {
                                    article?.let { it1 -> viewModel.saveArticle(it1) }
                                    ivSave.setImageResource(R.drawable.bookmark)
                                    Snackbar.make(
                                        requireView(),
                                        "Article saved successfully",
                                        Snackbar.LENGTH_SHORT
                                    ).apply {
                                        setAction("Show") {
                                            findNavController().navigate(R.id.action_categoryFragment_to_favouriteFragment)
                                            val navBar =
                                                requireActivity().findViewById<BottomNavigationView>(
                                                    R.id.bottom_navigation
                                                )
                                            navBar.gone()
                                        }
                                        show()
                                    }
                                }
                                check = false

                            }
                        }
                    }
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


}