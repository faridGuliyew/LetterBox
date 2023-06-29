package com.example.letterboxf.ui.detailFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.letterboxf.adapter.viewPager.MovieDetailTabLayoutViewPagerAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.ui.bottomsheetFragments.AddMovieFragment
import com.example.letterboxf.databinding.FragmentMovieDetailBinding
import com.example.letterboxf.model.apiModels.movies.Genre
import com.example.letterboxf.model.apiModels.movies.MovieDetailsResponse
import com.example.letterboxf.model.apiModels.movies.ProductionCompany
import com.example.letterboxf.model.apiModels.movies.ProductionCountry
import com.example.letterboxf.model.apiModels.movies.SpokenLanguage
import com.example.letterboxf.viewmodel.SameForAllViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>(FragmentMovieDetailBinding::inflate) {

    private val args : MovieDetailFragmentArgs by navArgs()
    private val sameForAllViewModel: SameForAllViewModel by viewModels()
    private lateinit var movie : MovieDetailsResponse

    override fun onViewCreatedLight() {
        observeViewModel()
        setViewPager(binding.viewPagerForMovieDetailTabLayout)
        goToAllReviews(binding.textViewAllReviews)
        showAddMovieFragment(binding.imageViewMoviePoster)
        goBack(binding.arrowBack)
    }

    private fun observeViewModel(){
        with(sameForAllViewModel){
            getMovieDetails(args.movieId)
            movieDetails.observe(viewLifecycleOwner){
                if (it!=null){
                    movie = it
                    binding.movie = it
                    setExtras(binding.chipImdb)
                }else{
                    movie = MovieDetailsResponse(false,"", Any(),1, listOf(Genre(0,"")),"",args.movieId,"","","", "",0.0,"", listOf(ProductionCompany(0,"","","")), listOf(ProductionCountry("","")),"",1,1, listOf(SpokenLanguage("","","")),"","","Unable to fetch movie name",false,0.0,0)
                    Toast.makeText(context,"Details of the film are not available",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setViewPager(viewPager : ViewPager2){
        viewPager.isUserInputEnabled = false
        val tabNames = arrayOf("CAST + CREW","DETAILS")
        viewPager.adapter = MovieDetailTabLayoutViewPagerAdapter(childFragmentManager,lifecycle)
        TabLayoutMediator(binding.tabLayoutMovieDetail,viewPager)
        { tab, position -> tab.text =  tabNames[position]}.attach()
    }

    private fun goToAllReviews(text : TextView){
        text.setOnClickListener {
            findNavController().navigate(MovieDetailFragmentDirections.actionMovieDetailFragmentToMovieReviewsMainFragment())
        }
    }

    private fun showAddMovieFragment(imageView: ImageView){
        imageView.setOnLongClickListener {
            findNavController().navigate(MovieDetailFragmentDirections.actionMovieDetailFragmentToAddMovieFragment(movie))
            true
        }
    }

    private fun goBack(button: Button){
        button.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setExtras(imdb : Chip){
        imdb.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,"https://www.imdb.com/title/${movie.imdbId}/".toUri()))
        }
    }
}