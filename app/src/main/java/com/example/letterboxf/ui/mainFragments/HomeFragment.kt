package com.example.letterboxf.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.letterboxf.adapter.recyclerView.PopularMoviesAdapter
import com.example.letterboxf.adapter.recyclerView.firebase.FirebaseFriendMovieAdapter
import com.example.letterboxf.adapter.recyclerView.firebase.FirebaseMovieAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentHomeBinding
import com.example.letterboxf.viewmodel.HomeViewModel
import com.example.letterboxf.viewmodel.PopularViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val homeViewModel : HomeViewModel by viewModels()
    private val popularMoviesAdapter = PopularMoviesAdapter()
    private val firebaseMoviesAdapter = FirebaseFriendMovieAdapter()

    override fun onViewCreatedLight() {
        observeViewModel()
        setPopularRv()
        setFriendsRv()
    }


    private fun observeViewModel(){
        with(homeViewModel){
            getPopularMovies(1)
            popularMovies.observe(viewLifecycleOwner){
                popularMoviesAdapter.updateAdapter(it)
            }
            getFriendMovies()
            friendsMovies.observe(viewLifecycleOwner){
                if (it.isEmpty()){
                    binding.textViewFollowing.visibility = View.GONE
                }else{
                    binding.textViewFollowing.visibility = View.VISIBLE
                    firebaseMoviesAdapter.updateAdapter(it)
                }

            }
        }
    }

    private fun setPopularRv(){
        val rv = binding.popularRv
        rv.adapter = popularMoviesAdapter
        popularMoviesAdapter.onClick={
            setSp(it)
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToMovieDetailFragment(it))
        }
        popularMoviesAdapter.onLongClick={
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToAddMovieFragment(it))
        }
    }

    private fun setFriendsRv(){
        val rv = binding.rvFriends
        rv.adapter = firebaseMoviesAdapter
        firebaseMoviesAdapter.onClick={
            setSp(it)
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToMovieDetailFragment(it))
        }
    }

    private fun setSp(movieId:String){
        val sp = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE)
        sp.edit().putString("movie",movieId).apply()
    }

}