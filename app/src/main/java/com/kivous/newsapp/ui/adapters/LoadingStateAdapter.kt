package com.kivous.newsapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kivous.newsapp.databinding.ErrorStateBinding

class LoadingStateAdapter :
    LoadStateAdapter<LoadingStateAdapter.ViewHolder>() {
    class ViewHolder(val binding: ErrorStateBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding = ErrorStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.apply {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                progressBar2.isVisible = loadState is LoadState.Error
            }
        }
    }
}