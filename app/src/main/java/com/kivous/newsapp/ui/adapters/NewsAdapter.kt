package com.kivous.newsapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.gson.Gson
import com.kivous.newsapp.R
import com.kivous.newsapp.data.model.Article
import com.kivous.newsapp.databinding.ListNewsBinding
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import com.kivous.newsapp.utils.Common.convertToISTFormat
import com.kivous.newsapp.utils.Common.shareArticle
import com.kivous.newsapp.utils.Constants.KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsAdapter(
    private val fragment: Fragment,
    private val viewModel: NewsViewModel,
    private val destinationId: Int
) : PagingDataAdapter<Article, NewsAdapter.ViewHolder>(Comparator) {

    class ViewHolder(val binding: ListNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        article?.let { article ->
            holder.apply {
                binding.apply {
                    tvTitle.text = article.title
                    tvSource.text = article.source?.name
                    tvPublishAt.text = article.publishedAt?.let { convertToISTFormat(it) }
                    ivArticle.load(article.urlToImage)

                    cvShare.setOnClickListener {
                        article.url?.let { url ->
                            shareArticle(fragment.requireActivity(), url)
                        }
                    }

                    article.url?.let { url ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val articleCount = viewModel.isArticleExist(url)
                            withContext(Dispatchers.Main) {
                                if (articleCount != 0) {
                                    ivSave.setImageResource(R.drawable.bookmark)
                                } else {
                                    ivSave.setImageResource(R.drawable.bookmark_border)
                                }
                            }
                        }

                        cvSave.setOnClickListener {
                            CoroutineScope(Dispatchers.IO).launch {
                                val articleCount = viewModel.isArticleExist(url)
                                withContext(Dispatchers.Main) {
                                    if (articleCount != 0) {
                                        viewModel.deleteArticleByUrl(url).also {
                                            binding.ivSave.setImageResource(R.drawable.bookmark_border)
                                        }
                                    } else {
                                        viewModel.insertArticle(article).also {
                                            binding.ivSave.setImageResource(R.drawable.bookmark)
                                        }
                                    }
                                }
                            }
                        }

                    }

                }

                itemView.setOnClickListener {
                    val bundle = bundleOf(KEY to Gson().toJson(article))
                    fragment.findNavController().navigate(destinationId, bundle)
                }

            }

        }
    }
}