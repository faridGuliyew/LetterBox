package com.example.letterboxf.adapter.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.letterboxf.ui.detailFragments.AllReviewsFragment
import com.example.letterboxf.ui.detailFragments.FriendReviewsFragment
import com.example.letterboxf.ui.detailFragments.OwnerOnlyFragment
import com.example.letterboxf.ui.navigationViewFragment.UserReviewsFragment

class MoviesReviewsTabLayoutViewPagerAdapter (private val fragmentManager: FragmentManager, val lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> AllReviewsFragment()
            1->FriendReviewsFragment()
            2-> OwnerOnlyFragment()
            else->Fragment()
        }
    }
}