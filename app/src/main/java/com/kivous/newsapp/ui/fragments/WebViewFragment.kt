package com.kivous.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.kivous.newsapp.R
import com.kivous.newsapp.data.model.Article
import com.kivous.newsapp.databinding.FragmentWebViewBinding
import com.kivous.newsapp.ui.viewmodels.NewsViewModel
import com.kivous.newsapp.utils.Common
import com.kivous.newsapp.utils.Common.gone
import com.kivous.newsapp.utils.Common.visible
import com.kivous.newsapp.utils.Constants.KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class WebViewFragment : Fragment() {
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var article: Article
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jsonArticle = arguments?.getString(KEY)
        article = Gson().fromJson(jsonArticle, Article::class.java)

        binding.cvBackArrow.setOnClickListener {
            findNavController().navigateUp()
        }

        setUpWebView()
        shareArticle()
        articleSaveStatus()
        saveAndDeleteArticle()
    }

    override fun onStart() {
        super.onStart()
        bottomNav = requireActivity().findViewById(R.id.bottom_navigation)
        bottomNav.gone()
    }

    override fun onStop() {
        super.onStop()
        bottomNav.visible()
        binding.webView.destroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun shareArticle() {
        binding.cvShare.setOnClickListener {
            article.url?.let {
                Common.shareArticle(requireActivity(), it)
            }
        }
    }

    private fun articleSaveStatus() {
        article.url?.let { url ->
            CoroutineScope(Dispatchers.IO).launch {
                val articleAlreadySaved = viewModel.isArticleAlreadySaved(url)
                withContext(Dispatchers.Main) {
                    if (articleAlreadySaved) {
                        binding.ivSave.setImageResource(R.drawable.bookmark)
                    } else {
                        binding.ivSave.setImageResource(R.drawable.bookmark_border)
                    }
                }
            }
        }
    }

    private fun saveAndDeleteArticle() {
        binding.cvSave.setOnClickListener {
            article.url?.let { url ->
                lifecycleScope.launch(Dispatchers.IO) {
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

    private fun setUpWebView() {
        article.url?.let { url ->
            binding.webView.apply {
                settings.javaScriptEnabled = false
                loadUrl(url)
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        binding.apply {
                            progressBar.gone()
                            webView.visible()
                            cvSave.visible()
                            cvShare.visible()
                        }
                    }
                }
            }
        }
    }

}