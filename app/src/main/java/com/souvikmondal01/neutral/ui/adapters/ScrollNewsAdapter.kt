package com.souvikmondal01.neutral.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.souvikmondal01.neutral.data.model.Article
import com.souvikmondal01.neutral.databinding.ListScrollNewsBinding
import javax.inject.Inject

class ScrollNewsAdapter @Inject constructor() :
    PagingDataAdapter<Article, ScrollNewsAdapter.ViewHolder>(Comparator) {
    class ViewHolder(val binding: ListScrollNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListScrollNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        article?.let {
            holder.binding.apply {
                tvTitle.text = it.title
                tvSource.text = it.source?.name
                tvDescription.text = it.description
                ivArticle.load(it.urlToImage)
            }
        }
    }

}

