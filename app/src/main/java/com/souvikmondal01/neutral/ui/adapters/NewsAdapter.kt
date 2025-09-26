package com.souvikmondal01.neutral.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.gson.Gson
import com.souvikmondal01.neutral.R
import com.souvikmondal01.neutral.data.model.Article
import com.souvikmondal01.neutral.databinding.ListNewsBinding
import com.souvikmondal01.neutral.ui.viewmodels.NewsViewModel
import com.souvikmondal01.neutral.utils.Common.convertUtcToIndianTime
import com.souvikmondal01.neutral.utils.Common.shareArticle
import com.souvikmondal01.neutral.utils.Constants.KEY
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
                    tvPublishAt.text = article.publishedAt?.convertUtcToIndianTime()
                    ivArticle.load(article.urlToImage)

                    // Share article
                    cvShare.setOnClickListener {
                        article.url?.let { url ->
                            shareArticle(fragment.requireActivity(), url)
                        }
                    }

                    // Check if article is already saved or not
                    article.url?.let { url ->
                        CoroutineScope(Dispatchers.IO).launch {
                            val articleAlreadySaved = viewModel.isArticleAlreadySaved(url)
                            withContext(Dispatchers.Main) {
                                if (articleAlreadySaved) {
                                    ivSave.setImageResource(R.drawable.bookmark)
                                } else {
                                    ivSave.setImageResource(R.drawable.bookmark_border)
                                }
                            }
                        }

                        // Save or delete article
                        cvSave.setOnClickListener {
                            CoroutineScope(Dispatchers.IO).launch {
                                val articleAlreadySaved = viewModel.isArticleAlreadySaved(url)
                                withContext(Dispatchers.Main) {
                                    if (articleAlreadySaved) {
                                        viewModel.deleteArticleByUrl(url)
                                        binding.ivSave.setImageResource(R.drawable.bookmark_border)
                                    } else {
                                        viewModel.insertArticle(article)
                                        binding.ivSave.setImageResource(R.drawable.bookmark)
                                    }
                                }
                            }
                        }

                    }

                }

                // Navigate to WebView Fragment
                itemView.setOnClickListener {
                    val bundle = bundleOf(KEY to Gson().toJson(article))
                    fragment.findNavController().navigate(destinationId, bundle)
                }

            }

        }
    }


}

