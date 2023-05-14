package com.kivous.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kivous.newsapp.databinding.ListSearchNewsBinding
import com.kivous.newsapp.model.Article

class SearchNewsAdapter(private val listener: SearchNewsListener) :
    RecyclerView.Adapter<SearchNewsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListSearchNewsBinding) :
        RecyclerView.ViewHolder(binding.root)


    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListSearchNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = differ.currentList[position]

        holder.apply {
            binding.apply {
                tvTitle.text = article.title
                tvSource.text = article.source?.name
                tvPublishAt.text = article.publishedAt
                count.text = "${position + 1}"
            }
            itemView.apply {
                Glide.with(this).load(article.urlToImage).into(binding.ivArticle)
                setOnClickListener {
                    listener.onArticleClick(holder, article)
                }
            }
        }
    }

}

interface SearchNewsListener {
    fun onArticleClick(holder: SearchNewsAdapter.ViewHolder, article: Article)
}