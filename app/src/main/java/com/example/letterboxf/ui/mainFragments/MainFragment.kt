package com.example.letterboxf.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.letterboxf.R
import com.example.letterboxf.adapter.viewPager.PopularViewPagerAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    override fun onViewCreatedLight() {
        setViewPager(binding.viewPager)
        setToolbar()
        setNavDrawer()
        setNavActions(binding.navView)
    }

    private fun setToolbar(){
        val toolbar = binding.toolbar2
        toolbar.setTitleTextAppearance(context, R.style.RobotoBoldTextAppearance)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_search->findNavController().navigate(MainFragmentDirections.actionMainFragmentToSearchFragment())
            }
            true
        }
    }


    private fun setViewPager(viewPager : ViewPager2){
        viewPager.isUserInputEnabled = false
        val viewPagerAdapter = PopularViewPagerAdapter(childFragmentManager,lifecycle)
        val titles = arrayOf("FILMS","REVIEWS","PEOPLE")
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout,viewPager){tab,position->
            tab.text =titles[position]
        }.attach()
    }

    private fun setNavDrawer(){
        val toggle = ActionBarDrawerToggle(requireActivity(),binding.root,R.string.open,R.string.close)
        val toolbar = binding.toolbar2
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        toolbar.navigationIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_menu)
        //Set displayName
        val header = binding.navView.getHeaderView(0)
        header.findViewById<TextView>(R.id.textViewDisplayNameHeader).text = displayName()
        if (uri() != ""){
            Picasso.get().load(uri()).into(header.findViewById<CircleImageView>(R.id.circleImageView))
        }
    }

    private fun setNavActions(navView: NavigationView){
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.action_profile->{findNavController().navigate(MainFragmentDirections.actionMainFragmentToProfileFragment(null)) }
                R.id.action_liked->{findNavController().navigate(MainFragmentDirections.actionMainFragmentToLikedMoviesFragment())}
                R.id.action_watchlist->{findNavController().navigate(MainFragmentDirections.actionMainFragmentToWatchListedMoviesFragment())}
                R.id.action_following->{findNavController().navigate(MainFragmentDirections.actionMainFragmentToFollowingFragment())}
                R.id.action_reviews->{findNavController().navigate(MainFragmentDirections.actionMainFragmentToUserReviewsFragment())}
                else -> {}
            }
            true
        }
    }

    private fun uri() : String{
        return requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("uri","")!!
    }

    private fun displayName() : String{
        return requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("displayName","...")!!
    }
}
