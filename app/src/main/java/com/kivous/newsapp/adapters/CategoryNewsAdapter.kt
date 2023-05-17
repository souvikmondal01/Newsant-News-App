package com.kivous.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kivous.newsapp.databinding.ListNewsBinding
import com.kivous.newsapp.model.Article

class CategoryNewsAdapter(private val listener: CategoryNewsListener) :
    PagingDataAdapter<Article, CategoryNewsAdapter.ViewHolder>(COMPARATOR) {

    class ViewHolder(val binding: ListNewsBinding) : RecyclerView.ViewHolder(binding.root)
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        listener.handleListView(holder, article)
        if (article != null) {
            holder.apply {
                binding.apply {
                    tvTitle.text = article.title
                    tvSource.text = article.source?.name
                }
                itemView.apply {
                    Glide.with(this).load(article.urlToImage).into(binding.ivArticle)
                }
            }
        }
    }

}

interface CategoryNewsListener {
    fun handleListView(holder: CategoryNewsAdapter.ViewHolder, article: Article?)
}