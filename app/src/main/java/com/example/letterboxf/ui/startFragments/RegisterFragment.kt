package com.example.letterboxf.ui.startFragments


import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.letterboxf.MainActivity
import com.example.letterboxf.R
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentRegisterBinding
import com.example.letterboxf.viewmodel.FirebaseViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

        private val firebaseViewModel : FirebaseViewModel by viewModels()

        override fun onViewCreatedLight() {
            onClick(binding.buttonRegister)
            observeViewModel()
            goToLogin(binding.textViewLogin)
        }

        private fun observeViewModel(){
            with(firebaseViewModel){
                firebaseResponse.observe(viewLifecycleOwner){
                    it?.let {response->
                        if (response.status){
                            FancyToast.makeText(requireContext(), response.text, FancyToast.LENGTH_SHORT,
                                FancyToast.SUCCESS, R.drawable.ic_logo, false).show()
                            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToSplashFragment())
                        }else{
                            FancyToast.makeText(requireContext(), response.text, FancyToast.LENGTH_SHORT,
                                FancyToast.ERROR, R.drawable.ic_logo, false).show()
                        }
                    }
                }
                isLoading.observe(viewLifecycleOwner){
                    val progressBar = binding.progressBar
                    if (it){
                        progressBar.visibility = View.VISIBLE
                    }else{
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }

        private fun onClick(button : Button){
            button.setOnClickListener {
                val pwd = binding.editTextPassword.text.toString().trim()
                val email = binding.editTextEmail.text.toString().trim()
                val displayName = binding.editTextDisplayName.text.toString().trim()
                if (isValid(pwd)){
                    firebaseViewModel.register(email,pwd,displayName)
                }
            }
        }

        private fun goToLogin(loginText : TextView){
            loginText.setOnClickListener {
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
            }
        }

        private fun isValid(password:String) : Boolean{
            return if (password.length < 6){
                FancyToast.makeText(requireContext(), "The length of the password should at least be 6 characters long", FancyToast.LENGTH_SHORT,
                    FancyToast.INFO, R.drawable.ic_logo, false).show()
                false
            }else{
                true
            }
        }
}