package com.kivous.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kivous.newsapp.databinding.ListScrollNewsBinding
import com.kivous.newsapp.model.Article

class CategoryNewsAdapter : PagingDataAdapter<Article, CategoryNewsAdapter.ViewHolder>(COMPARATOR) {

    class ViewHolder(val binding: ListScrollNewsBinding) : RecyclerView.ViewHolder(binding.root)

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
            ListScrollNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.apply {
                binding.apply {
                    tvTitle.text = article.title
                    tvDescription.text = article.description
                    tvSource.text = article.source?.name
                    tvCount.text = (position + 1).toString()
                }
                itemView.apply {
                    Glide.with(this).load(article.urlToImage).into(binding.ivArticle)
                }
            }
        }
    }


}