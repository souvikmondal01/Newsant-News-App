package com.souvikmondal01.neutral.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.souvikmondal01.neutral.R
import com.souvikmondal01.neutral.databinding.FragmentCategoryBinding
import com.souvikmondal01.neutral.ui.adapters.TabLayoutAdapter
import com.souvikmondal01.neutral.utils.Common.isDarkMode
import com.souvikmondal01.neutral.utils.Common.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TabLayoutAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TabLayoutAdapter(childFragmentManager, lifecycle)

        binding.tabLayout.apply {
            addTab(binding.tabLayout.newTab().setText("Business"))
            addTab(binding.tabLayout.newTab().setText("Entertainment"))
            addTab(binding.tabLayout.newTab().setText("Health"))
            addTab(binding.tabLayout.newTab().setText("Science"))
            addTab(binding.tabLayout.newTab().setText("Sports"))
            addTab(binding.tabLayout.newTab().setText("Technology"))
        }

        binding.viewpager.adapter = adapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    binding.viewpager.currentItem = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

    }

    override fun onStart() {
        super.onStart()
        if (isDarkMode(requireContext())) {
            setStatusBarColor(R.color.md_theme_dark_surface)
        } else {
            setStatusBarColor(R.color.md_theme_light_surface)
        }
    }

    override fun onStop() {
        super.onStop()
        if (isDarkMode(requireContext())) {
            setStatusBarColor(R.color.md_theme_dark_background)
        } else {
            setStatusBarColor(R.color.md_theme_light_background)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}