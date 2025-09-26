package com.souvikmondal01.neutral.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.souvikmondal01.neutral.data.model.Article
import com.souvikmondal01.neutral.databinding.ListSearchNewsBinding
import com.souvikmondal01.neutral.utils.Common.convertUtcToIndianTime

class SearchNewsAdapter(
    private val onNewsClicked: (Article) -> Unit
) : PagingDataAdapter<Article, SearchNewsAdapter.ViewHolder>(Comparator) {
    class ViewHolder(val binding: ListSearchNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListSearchNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                }
                itemView.setOnClickListener {
                    onNewsClicked(article)
                }
            }
        }
    }
}



