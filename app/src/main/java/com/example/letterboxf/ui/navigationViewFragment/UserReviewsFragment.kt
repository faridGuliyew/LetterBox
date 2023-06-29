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
import com.example.letterboxf.adapter.recyclerView.firebase.FirebaseReviewAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentUserReviewsBinding
import com.example.letterboxf.viewmodel.FirebaseDatabaseViewModel
import com.example.letterboxf.viewmodel.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserReviewsFragment : BaseFragment<FragmentUserReviewsBinding>(FragmentUserReviewsBinding::inflate) {
    private val reviewViewModel : ReviewViewModel by viewModels()
    private val firebaseReviewAdapter = FirebaseReviewAdapter()

    private lateinit var lottie : LottieAnimationView

    override fun onViewCreatedLight() {
        lottie = binding.lottie
        observe()
        reviewViewModel.getUserReview(null,null)
        setRv()
    }

    private fun observe(){
        with(reviewViewModel){
            reviews.observe(viewLifecycleOwner){
                if (it.isEmpty()){
                    binding.textViewHeader.text = "You do not have any reviews yet"
                    lottie.visibility = View.VISIBLE
                }else{
                    binding.textViewHeader.text = "Your reviews:"
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
            findNavController().navigate(UserReviewsFragmentDirections.actionUserReviewsFragmentToMovieDetailFragment(it))
        }

    }

    private fun setSp(movieId:String){
        val sp = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE)
        sp.edit().putString("movie",movieId).apply()
    }

}