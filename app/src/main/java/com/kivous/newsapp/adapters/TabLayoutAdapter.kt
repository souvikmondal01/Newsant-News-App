package com.kivous.newsapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kivous.newsapp.ui.fragments.categories.BusinessFragment
import com.kivous.newsapp.ui.fragments.categories.EntertainmentFragment
import com.kivous.newsapp.ui.fragments.categories.GeneralFragment
import com.kivous.newsapp.ui.fragments.categories.HealthFragment
import com.kivous.newsapp.ui.fragments.categories.ScienceFragment
import com.kivous.newsapp.ui.fragments.categories.SportsFragment
import com.kivous.newsapp.ui.fragments.categories.TechnologyFragment

class TabLayoutAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 7
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
                return GeneralFragment()
            }

            3 -> {
                return HealthFragment()
            }

            4 -> {
                return ScienceFragment()
            }

            5 -> {
                return SportsFragment()
            }

            else -> {
                return TechnologyFragment()
            }
        }

    }
}