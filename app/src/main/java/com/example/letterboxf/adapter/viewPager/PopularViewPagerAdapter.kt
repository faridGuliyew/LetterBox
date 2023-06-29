package com.example.letterboxf.adapter.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.letterboxf.ui.mainFragments.HomeFragment
import com.example.letterboxf.ui.mainFragments.PopularPeopleFragment
import com.example.letterboxf.ui.mainFragments.ReviewsFragment

class PopularViewPagerAdapter (fragmentManager : FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> HomeFragment()
            1-> ReviewsFragment()
            2-> PopularPeopleFragment()
            else -> Fragment()
        }
    }

}