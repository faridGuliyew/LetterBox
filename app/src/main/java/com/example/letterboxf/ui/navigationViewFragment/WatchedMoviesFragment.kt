package com.example.letterboxf.ui.navigationViewFragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.example.letterboxf.R
import com.example.letterboxf.adapter.recyclerView.firebase.FirebaseMovieAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentWatchedMoviesBinding
import com.example.letterboxf.viewmodel.FirebaseDatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchedMoviesFragment : BaseFragment<FragmentWatchedMoviesBinding>(FragmentWatchedMoviesBinding::inflate) {
    private val firebaseDatabaseViewModel : FirebaseDatabaseViewModel by viewModels()
    private val firebaseMovieAdapter = FirebaseMovieAdapter()

    private val args : WatchedMoviesFragmentArgs by navArgs()

    private lateinit var lottie : LottieAnimationView

    override fun onViewCreatedLight() {
        lottie = binding.lottie
        observe()
        firebaseDatabaseViewModel.getWatched(args.uid)
        setRv()
    }

    private fun observe(){
        with(firebaseDatabaseViewModel){
            movies.observe(viewLifecycleOwner){
                if (it.isEmpty()){
                    lottie.visibility = View.VISIBLE
                    binding.textViewHeader.text = "You have not watched any movies yet"
                }else{
                    lottie.visibility = View.GONE
                    firebaseMovieAdapter.updateAdapter(it)
                    binding.textViewHeader.text = "Watched films:"
                }
            }
        }
    }

    private fun setRv(){
        val rv = binding.rv
        rv.adapter = firebaseMovieAdapter
        firebaseMovieAdapter.onClick={
            setSp(it)
            findNavController().navigate(WatchedMoviesFragmentDirections.actionWatchedMoviesFragmentToMovieDetailFragment(it))
        }
    }

    private fun setSp(movieId:String){
        val sp = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE)
        sp.edit().putString("movie",movieId).apply()
    }

}