package com.kivous.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.kivous.newsapp.R
import com.kivous.newsapp.adapters.TabLayoutAdapter
import com.kivous.newsapp.common.Utils
import com.kivous.newsapp.common.Utils.hideKeyboard
import com.kivous.newsapp.common.Utils.setStatusBarColor
import com.kivous.newsapp.common.Utils.visible
import com.kivous.newsapp.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: TabLayoutAdapter
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
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Business"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Entertainment"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("General"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Health"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Science"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sports"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Technology"))

        binding.viewpager.adapter = adapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    binding.viewpager.currentItem = tab.position
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
        hideKeyboard()
        val navBar =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visible()
    }

    override fun onResume() {
        super.onResume()
        if (Utils.isDarkMode(requireContext())) {
            setStatusBarColor(R.color.arsenic)
        }
    }

    override fun onPause() {
        super.onPause()
        if (Utils.isDarkMode(requireContext())) {
            setStatusBarColor(R.color.background)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}