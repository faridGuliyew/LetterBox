package com.example.letterboxf.ui.bottomsheetFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.letterboxf.R
import com.example.letterboxf.databinding.FragmentAddMovieBinding
import com.example.letterboxf.model.firebase.FirebaseMovieModel
import com.example.letterboxf.ui.detailFragments.MovieDetailFragment
import com.example.letterboxf.ui.detailFragments.MovieDetailFragmentDirections
import com.example.letterboxf.ui.mainFragments.MainFragment
import com.example.letterboxf.ui.mainFragments.MainFragmentDirections
import com.example.letterboxf.ui.mainFragments.SearchFragment
import com.example.letterboxf.ui.mainFragments.SearchFragmentDirections
import com.example.letterboxf.utils.Constants.IMAGE_BASE
import com.example.letterboxf.viewmodel.FirebaseDatabaseViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddMovieFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentAddMovieBinding? = null
    private val binding
        get() = _binding!!

    private val args : AddMovieFragmentArgs by navArgs()

    private val firebaseDatabaseViewModel : FirebaseDatabaseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMovieBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        firebaseDatabaseViewModel.getMovieActivity(args.movie.id)
        manageClicks(binding.watched,binding.liked,binding.watchlist)
        bindViews(binding.textViewTitleMovie)
        goToReview(binding.buttonReview,binding.ratingBar)
    }

    private fun observeViewModel(){
        with(firebaseDatabaseViewModel){
            response.observe(viewLifecycleOwner){
                it.let { r->
                    if (!r!!.status){
                        //FancyToast.makeText(requireContext(),r.text,FancyToast.LENGTH_SHORT,FancyToast.ERROR,R.drawable.ic_logo,false).show()
                    }
                }
            }
            movieActivity.observe(viewLifecycleOwner){movieActivity->
                val watched = binding.watched
                val liked = binding.liked
                val watchListed = binding.watchlist
                with(movieActivity){
                    Log.e("REAL COWBOY",this.toString())
                    if(isWatched){
                        watched.tag = "enabled"
                        watched.setImageResource(R.drawable.ic_watched_enabled)
                    }
                    if(isLiked){
                        liked.tag = "enabled"
                        liked.setImageResource(R.drawable.ic_like_enabled)
                    }
                    if(isWatchListed){
                        watchListed.tag = "enabled"
                        watchListed.setImageResource(R.drawable.ic_watchlist_enabled)
                    }
                    binding.ratingBar.rating = movieActivity.rating.toFloat()
                    rateMovie(binding.ratingBar)
                }
            }
        }
    }

    private fun manageClicks(watched:ImageView,like:ImageView,watchlist:ImageView){
        val movie = args.movie
        var movieModel : FirebaseMovieModel
        watched.setOnClickListener {
            movieModel = FirebaseMovieModel(movie.id,movie.title,movie.posterPath ?: "broken link")
            if (watched.tag == "disabled"){
                watched.tag = "enabled"
                watched.setImageResource(R.drawable.ic_watched_enabled)
                firebaseDatabaseViewModel.addToWatched(movieModel)
            }else{
                watched.tag = "disabled"
                watched.setImageResource(R.drawable.ic_watched)
                firebaseDatabaseViewModel.removeFromWatched(movie.id)
            }
        }
        like.setOnClickListener {
            movieModel = FirebaseMovieModel(movie.id,movie.title,movie.posterPath?:"broken link", liked = true)
            if (like.tag == "disabled"){
                like.tag = "enabled"
                like.setImageResource(R.drawable.ic_like_enabled)
                firebaseDatabaseViewModel.addToLiked(movieModel)
            }else{
                like.tag = "disabled"
                like.setImageResource(R.drawable.ic_like)
                firebaseDatabaseViewModel.removeFromLiked(movie.id)
            }
        }
        watchlist.setOnClickListener {
            movieModel = FirebaseMovieModel(movie.id,movie.title,movie.posterPath?:"Broken link")
            if (watchlist.tag == "disabled"){
                watchlist.tag = "enabled"
                watchlist.setImageResource(R.drawable.ic_watchlist_enabled)
                firebaseDatabaseViewModel.addToWatchList(movieModel)
            }else{
                watchlist.tag = "disabled"
                watchlist.setImageResource(R.drawable.ic_watchlist)
                firebaseDatabaseViewModel.removeFromWatchListed(movie.id)
            }
        }
    }


    private fun rateMovie(ratingBar : RatingBar){
        ratingBar.setOnRatingBarChangeListener { _, fl, b ->
            firebaseDatabaseViewModel.rateMovie(args.movie.id,fl.toDouble())
        }
    }

    private fun bindViews(textView: TextView){
        textView.text = args.movie.title
    }

    private fun goToReview(button: ImageButton,ratingBar: RatingBar){
        button.setOnClickListener {
            val movie = args.movie
            val obj = FirebaseMovieModel(movie.id,movie.title,movie.posterPath?:"broken link",ratingBar.rating.toDouble())
            findNavController().navigate(AddMovieFragmentDirections.actionAddMovieFragmentToAddReviewFragment(obj))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}