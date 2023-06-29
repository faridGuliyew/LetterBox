package com.example.letterboxf.ui.mainFragments

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.letterboxf.R
import com.example.letterboxf.adapter.viewPager.MoviesReviewsTabLayoutViewPagerAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentMovieReviewsMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MovieReviewsMainFragment : BaseFragment<FragmentMovieReviewsMainBinding>(FragmentMovieReviewsMainBinding::inflate) {

    override fun onViewCreatedLight() {
        setToolbar()
        setViewPager(binding.viewPager)
    }

    private fun setToolbar(){
        val toolbar  = binding.toolbar
        toolbar.setTitleTextAppearance(context, R.style.RobotoBoldTextAppearance);
        toolbar.navigationIcon = getDrawable(requireContext(),R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setViewPager(viewPager : ViewPager2){
        viewPager.adapter = MoviesReviewsTabLayoutViewPagerAdapter(childFragmentManager,lifecycle)
        val titles = arrayOf("Everyone","Friends","You")
        TabLayoutMediator(binding.tabLayout,viewPager){tab,position->
            tab.text = titles[position]
        }.attach()
    }
}