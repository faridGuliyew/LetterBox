package com.example.letterboxf.ui.startFragments

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.letterboxf.MainActivity
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentSplashBinding
import com.example.letterboxf.viewmodel.ProfileCheckViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val profileCheckViewModel : ProfileCheckViewModel by viewModels()
    private var isLoaded = false
    override fun onViewCreatedLight() {

        observe()
        profileCheckViewModel.fetchDisplayName()
        profileCheckViewModel.fetchProfilePhoto()
        goToLogin(binding.buttonStart)
    }

    private fun observe(){
        with(profileCheckViewModel){
            response.observe(viewLifecycleOwner){
                Log.e("PHASE","1: $isLoaded")
                when(it.status){
                    true->{
                        setDisplayNameAndUid(it.param1,it.param2)
                        if (isLoaded){
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        }
                        isLoaded=true
                    }
                    else->{
                        binding.progressBarSplash.visibility = View.GONE
                        binding.buttonStart.visibility = View.VISIBLE
                    }
                }
            }
            uri.observe(viewLifecycleOwner){
                Log.e("PHASE","2: $isLoaded")
                setUri(it.toString())
                Log.e("PHASE",it.toString())
                if (isLoaded){
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
                isLoaded = true
            }
        }
    }

    private fun goToLogin(button : Button){
        button.setOnClickListener {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }
    }

    private fun setDisplayNameAndUid(displayName : String, uid : String) {
        val editor = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit()
        editor.putString("displayName", displayName)
            .putString("uid",uid)
            .apply()
    }

    private fun setUri(uri : String){
        requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit().putString("uri",uri).apply()
    }
}