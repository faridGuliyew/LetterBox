package com.example.letterboxf.ui.viewpagerFragments

import android.content.Context
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.letterboxf.adapter.recyclerView.CastAdapter
import com.example.letterboxf.adapter.recyclerView.CrewAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentCrewAndCastBinding
import com.example.letterboxf.viewmodel.SameForAllViewModel
import com.example.letterboxf.ui.detailFragments.MovieDetailFragmentDirections
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CrewAndCastFragment : BaseFragment<FragmentCrewAndCastBinding>(FragmentCrewAndCastBinding::inflate) {

    private val sameForAllViewModel: SameForAllViewModel by viewModels()
    private val castAdapter = CastAdapter()
    private val crewAdapter = CrewAdapter()

    override fun onViewCreatedLight() {
        observeViewModel()
        setRv()
    }


    private fun observeViewModel(){
        with(sameForAllViewModel){
            getMovieCredits(getSp())
            movieCredits.observe(viewLifecycleOwner){
                castAdapter.updateAdapter(it.cast)
                crewAdapter.updateAdapter(it.crew)
                Log.e("COWBOY FROM CAST",it.cast.toString())
            }
        }
    }
    private fun setRv(){
        val castRv = binding.rvCast
        castRv.adapter = castAdapter
        castAdapter.onClick={
            setActorSp(it)
            requireParentFragment().view?.findNavController()?.navigate(
                MovieDetailFragmentDirections.actionMovieDetailFragmentToPersonDetailFragmentMain(it))
        }
        val crewRv = binding.rvCrew
        crewRv.adapter = crewAdapter
        crewAdapter.onClick={
            setActorSp(it)
            requireParentFragment().view?.findNavController()?.navigate(MovieDetailFragmentDirections.actionMovieDetailFragmentToPersonDetailFragmentMain(it))
        }
    }

    private fun getSp() : String{
        return requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE).getString("movie","0")!!
    }

    private fun setActorSp(actorId : String){
        val sp = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE)
        sp.edit().putString("actor",actorId).apply()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}