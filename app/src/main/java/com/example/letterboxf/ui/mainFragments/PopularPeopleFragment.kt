package com.example.letterboxf.ui.mainFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letterboxf.adapter.recyclerView.PopularPeopleHorizontalAdapter
import com.example.letterboxf.databinding.FragmentPopularPeopleBinding
import com.example.letterboxf.viewmodel.PopularViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PopularPeopleFragment : Fragment() {
    private var _binding : FragmentPopularPeopleBinding? = null
    private val binding
        get() = _binding!!
    private val popularPeopleHorizontalAdapter = PopularPeopleHorizontalAdapter()
    private val popularViewModel: PopularViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopularPeopleBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setRv()
    }


    private fun observeViewModel(){
        with(popularViewModel){
            getPopularPeople(1)
            popularPeople.observe(viewLifecycleOwner){
                Log.e("COWBOY IN POPULAR PEOPLE LIST:",it.toString())
                popularPeopleHorizontalAdapter.updateAdapter(it)
            }
        }
    }

    private fun setRv(){
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = popularPeopleHorizontalAdapter
        popularPeopleHorizontalAdapter.onClick={
            setActorSp(it)
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToPersonDetailFragmentMain(
                    it
                )
            )
        }
    }

    private fun setActorSp(actorId : String){
        val sp = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE)
        sp.edit().putString("actor",actorId).apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}