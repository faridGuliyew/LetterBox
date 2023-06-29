package com.example.letterboxf.ui.navigationViewFragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.letterboxf.adapter.recyclerView.firebase.FirebaseMovieAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentWatchListedMoviesBinding
import com.example.letterboxf.viewmodel.FirebaseDatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchListedMoviesFragment : BaseFragment<FragmentWatchListedMoviesBinding>(FragmentWatchListedMoviesBinding::inflate) {

    private val firebaseDatabaseViewModel : FirebaseDatabaseViewModel by viewModels()
    private val firebaseMovieAdapter = FirebaseMovieAdapter()

    private lateinit var lottie : LottieAnimationView

    override fun onViewCreatedLight() {
        lottie = binding.lottie
        observe()
        firebaseDatabaseViewModel.getWatchListed(null)
        setRv()
    }

    private fun observe(){
        with(firebaseDatabaseViewModel){
            movies.observe(viewLifecycleOwner){
                if (it.isEmpty()){
                    binding.textViewHeader.text = "No movies are added to the watch list yet"
                    lottie.visibility = View.VISIBLE
                }else{
                    binding.textViewHeader.text = "Watchlisted movies:"
                    lottie.visibility = View.GONE
                    firebaseMovieAdapter.updateAdapter(it)
                }
            }
        }
    }

    private fun setRv(){
        val rv = binding.rv
        rv.adapter = firebaseMovieAdapter
        firebaseMovieAdapter.onClick={
            setSp(it)
            findNavController().navigate(WatchListedMoviesFragmentDirections.actionWatchListedMoviesFragmentToMovieDetailFragment(it))
        }
    }

    private fun setSp(movieId:String){
        val sp = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE)
        sp.edit().putString("movie",movieId).apply()
    }

}