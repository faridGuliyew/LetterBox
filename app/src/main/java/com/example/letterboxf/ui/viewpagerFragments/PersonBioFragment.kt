package com.example.letterboxf.ui.viewpagerFragments

import android.content.Context
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentPersonBioBinding
import com.example.letterboxf.viewmodel.SameForAllViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PersonBioFragment : BaseFragment<FragmentPersonBioBinding>(FragmentPersonBioBinding::inflate) {

    private val sameForAllViewModel : SameForAllViewModel by viewModels()

    override fun onViewCreatedLight() {
        observeViewModel()
    }


    private fun observeViewModel(){
        with(sameForAllViewModel){
            getActorDetails(getActorId())
            actorDetails.observe(viewLifecycleOwner){
                binding.actor = it
            }
        }
    }

    private fun getActorId() : String{
        return requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE).getString("actor","0")!!
    }
}