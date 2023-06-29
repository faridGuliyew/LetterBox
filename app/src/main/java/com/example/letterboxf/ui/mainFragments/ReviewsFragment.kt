package com.example.letterboxf.ui.mainFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letterboxf.adapter.recyclerView.PopularReviewsAdapter
import com.example.letterboxf.databinding.FragmentReviewsBinding
import com.example.letterboxf.viewmodel.PopularViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReviewsFragment : Fragment() {
    private var _binding : FragmentReviewsBinding? = null
    private val binding
        get() = _binding!!
    private val popularViewModel : PopularViewModel by activityViewModels()
    private val adapter = PopularReviewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setRv()
    }


    private fun observeViewModel(){
        with(popularViewModel){
           getPopularReviews(1)
            popularMovies.observe(viewLifecycleOwner){
                adapter.updateAdapter(it)
            }

        }
    }

    private fun setRv(){
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter
        adapter.onMovieClick={
            setSp(it)
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToMovieDetailFragment(it)
            )
        }
        adapter.onLongClick={
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToAddMovieFragment(it))
        }
    }

    private fun setSp(movieId:String){
        val sp = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE)
        sp.edit().putString("movie",movieId).apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}