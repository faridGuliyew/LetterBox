package com.example.letterboxf.ui.startFragments


import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.letterboxf.MainActivity
import com.example.letterboxf.R
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentLoginBinding
import com.example.letterboxf.viewmodel.FirebaseViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val firebaseViewModel : FirebaseViewModel by activityViewModels()

    override fun onViewCreatedLight() {
        onClick(binding.buttonLogin)
        observeViewModel()
        goToRegister(binding.textViewRegister)
    }

    private fun observeViewModel(){
        with(firebaseViewModel){
            firebaseResponse.observe(viewLifecycleOwner){
                it?.let {response->
                    if (response.status){
                        FancyToast.makeText(requireContext(), response.text, FancyToast.LENGTH_SHORT,FancyToast.SUCCESS, R.drawable.ic_logo, false).show()
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSplashFragment())
                    }else{
                        FancyToast.makeText(requireContext(), response.text, FancyToast.LENGTH_SHORT,FancyToast.ERROR, R.drawable.ic_logo, false).show()
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
            val pwd = binding.editTextPassword.text.toString()
            val email = binding.editTextEmail.text.toString().trim()
            if (isValid(pwd)){
                firebaseViewModel.login(email,pwd)
            }
        }
    }
    private fun goToRegister(registerText : TextView){ registerText.setOnClickListener { findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment()) } }

    private fun isValid(password:String) : Boolean{
        return if (password.length < 6){
            FancyToast.makeText(requireContext(),"The length of the password should be at least 6 characters",FancyToast.LENGTH_SHORT,FancyToast.INFO,R.drawable.ic_logo,false).show()
            false } else { true }
    }

}