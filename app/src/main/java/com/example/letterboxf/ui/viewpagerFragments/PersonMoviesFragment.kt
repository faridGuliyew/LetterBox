package com.example.letterboxf.ui.viewpagerFragments

import android.content.Context
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.letterboxf.adapter.recyclerView.person.PersonMoviesAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentPersonMoviesBinding
import com.example.letterboxf.viewmodel.SameForAllViewModel
import com.example.letterboxf.ui.detailFragments.PersonDetailFragmentMainDirections
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PersonMoviesFragment : BaseFragment<FragmentPersonMoviesBinding>(FragmentPersonMoviesBinding::inflate) {

    private val sameForAllViewModel: SameForAllViewModel by viewModels()
    private val castAdapter = PersonMoviesAdapter()

    override fun onViewCreatedLight() {
        setRv()
        observeViewModel()
    }


    private fun observeViewModel(){
        with(sameForAllViewModel){
            getPersonCredits(actorId())
            actorCredits.observe(viewLifecycleOwner){
                Log.e("COWBOY FROM ACTOR CREDITS",it.toString())
                castAdapter.updateAdapter(it.cast)
            }
        }
    }

    private fun setRv(){
        val rv = binding.rvCast
        rv.adapter = castAdapter
        castAdapter.onClick={
            findNavController().navigate(PersonDetailFragmentMainDirections.actionPersonDetailFragmentMainToMovieDetailFragment(it))
            setSp(it)
        }
    }
    private fun setSp(movieId:String){
        val sp = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE)
        sp.edit().putString("movie",movieId).apply()
    }

    private fun actorId() : String{
        return requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE).getString("actor","0")!!
    }
}