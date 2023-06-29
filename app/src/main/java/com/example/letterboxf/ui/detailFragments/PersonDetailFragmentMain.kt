package com.example.letterboxf.ui.detailFragments


import androidx.viewpager2.widget.ViewPager2
import com.example.letterboxf.adapter.viewPager.PersonDetailViewPagerAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentPersonDetailMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonDetailFragmentMain : BaseFragment<FragmentPersonDetailMainBinding>(FragmentPersonDetailMainBinding::inflate) {

    override fun onViewCreatedLight() {

        setTabLayout(binding.tabLayoutPeople,binding.viewPagerPeopleDetails)
    }

    private fun setTabLayout(tabLayout : TabLayout,viewPager : ViewPager2){
        viewPager.adapter = PersonDetailViewPagerAdapter(childFragmentManager,lifecycle)
        val titles = arrayOf("BIO","Movies")
        TabLayoutMediator(tabLayout,viewPager){tab,pos->
            tab.text = titles[pos]
        }.attach()
    }

}