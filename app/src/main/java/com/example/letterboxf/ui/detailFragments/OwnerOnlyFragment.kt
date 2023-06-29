package com.example.letterboxf.ui.detailFragments

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
import com.example.letterboxf.adapter.recyclerView.firebase.FirebaseReviewAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentOwnerOnlyBinding
import com.example.letterboxf.ui.mainFragments.MovieReviewsMainFragmentDirections
import com.example.letterboxf.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OwnerOnlyFragment : BaseFragment <FragmentOwnerOnlyBinding>(FragmentOwnerOnlyBinding::inflate){
    private val reviewViewModel : ReviewViewModel by viewModels()
    private val firebaseReviewAdapter = FirebaseReviewAdapter()

    private lateinit var lottie : LottieAnimationView

    override fun onViewCreatedLight() {
        lottie = binding.lottie
        observe()
        reviewViewModel.getUserReview(null,getMovieId())
        setRv()
    }

    private fun observe(){
        with(reviewViewModel){
            reviews.observe(viewLifecycleOwner){
                if (it.isEmpty()){
                    lottie.visibility = View.VISIBLE
                }else{
                    lottie.visibility = View.GONE
                    firebaseReviewAdapter.updateAdapter(it)
                }
            }
        }
    }

    private fun setRv(){
        val rv = binding.rv
        rv.adapter = firebaseReviewAdapter
        firebaseReviewAdapter.onClick={
            setSp(it)
            findNavController().navigate(MovieReviewsMainFragmentDirections.actionMovieReviewsMainFragmentToMovieDetailFragment(it))
        }

    }

    private fun getMovieId() : String{
        return requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE).getString("movie","0")!!
    }

    private fun setSp(movieId:String){
        val sp = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE)
        sp.edit().putString("movie",movieId).apply()
    }

}