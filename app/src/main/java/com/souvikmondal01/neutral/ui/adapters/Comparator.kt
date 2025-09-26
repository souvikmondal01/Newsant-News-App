package com.souvikmondal01.neutral.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.souvikmondal01.neutral.data.model.Article

object Comparator : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem == newItem

}