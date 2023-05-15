package com.kivous.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kivous.newsapp.databinding.ListScrollNewsBinding
import com.kivous.newsapp.model.Article

class ScrollNewsAdapter(private val list: MutableList<Article>) :
    RecyclerView.Adapter<ScrollNewsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListScrollNewsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListScrollNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = list[position]
        holder.apply {
            binding.apply {
                tvTitle.text = article.title
                tvDescription.text = article.description
                tvSource.text = article.source?.name
                tvCount.text = (position+1).toString()
            }
            itemView.apply {
                Glide.with(this).load(article.urlToImage).into(binding.ivArticle)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}