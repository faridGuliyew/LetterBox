package com.example.letterboxf.ui.viewpagerFragments

import android.content.Context
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.letterboxf.adapter.recyclerView.NormalReviewAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentReviewsViewPagerBinding
import com.example.letterboxf.viewmodel.SameForAllViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReviewsViewPagerFragment : BaseFragment<FragmentReviewsViewPagerBinding>(FragmentReviewsViewPagerBinding::inflate) {

    private val sameForAllViewModel: SameForAllViewModel by viewModels()
    private val reviewAdapter = NormalReviewAdapter()

    override fun onViewCreatedLight() {
        observeViewModel()
        setRv()
    }


    private fun observeViewModel(){
        with(sameForAllViewModel){
            getMovieReviews(getSp())
            movieReviews.observe(viewLifecycleOwner){
                reviewAdapter.updateAdapter(it)
            }
        }
    }

    private fun setRv(){
        val rv = binding.rv
        rv.adapter = reviewAdapter
    }

    private fun getSp() : String{
        return requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE).getString("movie","0")!!
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

}