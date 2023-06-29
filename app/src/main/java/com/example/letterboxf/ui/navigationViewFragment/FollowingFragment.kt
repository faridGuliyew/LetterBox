package com.example.letterboxf.ui.navigationViewFragment

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
import com.example.letterboxf.adapter.recyclerView.firebase.FirebaseUserAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentFollowingBinding
import com.example.letterboxf.model.firebase.FirebaseUserModel
import com.example.letterboxf.viewmodel.FirebaseDatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FollowingFragment : BaseFragment<FragmentFollowingBinding>(FragmentFollowingBinding::inflate) {

    private val firebaseDatabaseViewModel : FirebaseDatabaseViewModel by viewModels()
    private val firebaseFriendAdapter = FirebaseUserAdapter()
    private lateinit var lottie : LottieAnimationView
    override fun onViewCreatedLight() {
        lottie = binding.lottie
        observe()
        setRv()
    }

    private fun observe(){
        with(firebaseDatabaseViewModel){
            getAllFriends()
            friendsResponse.observe(viewLifecycleOwner){
                if (it.isEmpty()){
                    lottie.visibility = View.VISIBLE
                    binding.textViewHeader.text = "You do not follow any users"
                }else{
                    binding.textViewHeader.text = "Following:"
                    lottie.visibility = View.GONE
                    firebaseFriendAdapter.updateAdapter(it)
                }
            }
        }
    }


    private fun setRv(){
        val rv = binding.rv
        rv.adapter = firebaseFriendAdapter
        firebaseFriendAdapter.onClick={
            findNavController().navigate(FollowingFragmentDirections.actionFollowingFragmentToProfileFragment(it))
        }
    }

}