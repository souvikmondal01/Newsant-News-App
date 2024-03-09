package com.kivous.newsapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kivous.newsapp.ui.fragments.categories.BusinessFragment
import com.kivous.newsapp.ui.fragments.categories.EntertainmentFragment
import com.kivous.newsapp.ui.fragments.categories.HealthFragment
import com.kivous.newsapp.ui.fragments.categories.ScienceFragment
import com.kivous.newsapp.ui.fragments.categories.SportsFragment
import com.kivous.newsapp.ui.fragments.categories.TechnologyFragment

class TabLayoutAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return BusinessFragment()
            }

            1 -> {
                return EntertainmentFragment()
            }

            2 -> {
                return HealthFragment()
            }

            3 -> {
                return ScienceFragment()
            }

            4 -> {
                return SportsFragment()
            }

            else -> {
                return TechnologyFragment()
            }
        }
    }


}