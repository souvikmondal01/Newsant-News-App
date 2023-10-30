package com.kivous.newsapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kivous.newsapp.data.model.Article
import com.kivous.newsapp.databinding.ListSearchNewsBinding
import com.kivous.newsapp.utils.Common.convertToISTFormat
import com.kivous.newsapp.utils.Common.visible

class FavouriteNewsAdapter(
    private val onNewsClicked: (Article) -> Unit,
    private val onBookmarkClicked: (Article) -> Unit,
) : RecyclerView.Adapter<FavouriteNewsAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListSearchNewsBinding) : RecyclerView.ViewHolder(binding.root)

    val differ = AsyncListDiffer(this, Comparator)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListSearchNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = differ.currentList[position]
        article?.let { article ->
            holder.apply {
                binding.apply {
                    tvTitle.text = article.title
                    tvSource.text = article.source?.name
                    tvPublishAt.text = article.publishedAt?.let { convertToISTFormat(it) }
                    ivArticle.load(article.urlToImage)
                    cvSave.visible()
                    cvSave.setOnClickListener {
                        onBookmarkClicked(article)
                    }

                }
                itemView.setOnClickListener {
                    onNewsClicked(article)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}