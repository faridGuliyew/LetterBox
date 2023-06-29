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
import com.example.letterboxf.R
import com.example.letterboxf.adapter.recyclerView.firebase.FirebaseMovieAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentLikedMoviesBinding
import com.example.letterboxf.viewmodel.FirebaseDatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikedMoviesFragment : BaseFragment<FragmentLikedMoviesBinding>(FragmentLikedMoviesBinding::inflate) {

    private val firebaseDatabaseViewModel : FirebaseDatabaseViewModel by viewModels()
    private val firebaseMovieAdapter = FirebaseMovieAdapter()

    private lateinit var lottie : LottieAnimationView

    override fun onViewCreatedLight() {
        lottie = binding.lottie
        observe()
        firebaseDatabaseViewModel.getLiked(null)
        setRv()
    }

    private fun observe(){
        with(firebaseDatabaseViewModel){
            likedMovies.observe(viewLifecycleOwner){
                    if (it.isEmpty()){
                        lottie.visibility = View.VISIBLE
                        binding.textViewHeader.text = "You have not liked any movies yet"
                    }else{
                        binding.textViewHeader.text = "Liked films:"
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
            findNavController().navigate(LikedMoviesFragmentDirections.actionLikedMoviesFragmentToMovieDetailFragment(it))
        }
    }

    private fun setSp(movieId:String){
        val sp = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE)
        sp.edit().putString("movie",movieId).apply()
    }
}