package com.example.letterboxf.adapter.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.letterboxf.ui.viewpagerFragments.PersonBioFragment
import com.example.letterboxf.ui.viewpagerFragments.PersonMoviesFragment

class PersonDetailViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> PersonBioFragment()
            1-> PersonMoviesFragment()
            else->Fragment()
        }
    }
}