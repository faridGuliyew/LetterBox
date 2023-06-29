package com.example.letterboxf.ui.mainFragments


import android.content.Context
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxf.adapter.recyclerView.PopularMoviesAdapter
import com.example.letterboxf.adapter.recyclerView.firebase.FirebaseUserAdapter
import com.example.letterboxf.adapter.recyclerView.search.SearchPeopleAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentSearchBinding
import com.example.letterboxf.viewmodel.SearchViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private var searchMovies = true
    private var searchCast = false
    private var searchUsers = false

    private val movieAdapter = PopularMoviesAdapter()
    private val peopleAdapter = SearchPeopleAdapter()
    private val userAdapter = FirebaseUserAdapter()

    private var query = ""

    private val searchViewModel : SearchViewModel by viewModels()
    private lateinit var rv : RecyclerView

    override fun onViewCreatedLight() {
        initVars()
        observeViewModel()
        setAdapterFunctions()
        setSearchView(binding.searchView)
        setMode(binding.tabLayoutSearch)
    }

    private fun initVars(){
        rv = binding.searchRv
        rv.adapter = movieAdapter
    }

    private fun observeViewModel(){
        with(searchViewModel){
            movies.observe(viewLifecycleOwner){
                movieAdapter.updateAdapter(it)
            }
            people.observe(viewLifecycleOwner){
                peopleAdapter.updateAdapter(it)
            }
            users.observe(viewLifecycleOwner){
                Log.e("INSPECTOR COWBOY",it.toString())
                userAdapter.updateAdapter(it)
            }
        }
    }

    private fun getMovies(query : String){
        searchViewModel.getMovies(query)
    }

    private fun getPeople(query : String){
        searchViewModel.getPeople(query)
    }

    private fun getUsers(query: String){
        if (query.trim() != ""){
            searchViewModel.getUsers(query)
        }
    }

    private fun setAdapterFunctions(){
        movieAdapter.onClick={
            setSp(it)
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToMovieDetailFragment(
                    it
                )
            )
        }
        movieAdapter.onLongClick={
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToAddMovieFragment(it))
        }
        peopleAdapter.onClick={
            setActorSp(it)
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToPersonDetailFragmentMain(
                    it
                )
            )
        }

        userAdapter.onClick={
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToProfileFragment(it))
        }
    }

    private fun setSearchView(searchView : SearchView){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean { return false }

            override fun onQueryTextChange(text: String?): Boolean {
                query = text.orEmpty()
                binding.informativeText.visibility = View.GONE
                if (searchMovies){
                    getMovies(query)
                }
                else if (searchCast){
                    getPeople(query)
                }
                else if (searchUsers){
                    getUsers(query)
                }
                return true
            }

        })
    }

    private fun setMode(tabLayout : TabLayout){
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when(tab.position){
                       0-> { searchMovies = true
                           getMovies(query)
                           rv.adapter = movieAdapter
                           rv.layoutManager = GridLayoutManager(context,3)
                       }
                        1-> {searchCast = true;searchMovies = false
                            getPeople(query)
                            rv.adapter = peopleAdapter
                            rv.layoutManager = LinearLayoutManager(context)
                        }
                        2-> {searchUsers = true;searchMovies = false;searchCast=false
                            binding.informativeText.visibility = View.GONE
                            getUsers(query)
                            rv.adapter = userAdapter
                            rv.layoutManager = LinearLayoutManager(context)
                        }
                        else -> {}
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setSp(movieId:String){
        val sp = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE)
        sp.edit().putString("movie",movieId).apply()
    }

    private fun setActorSp(actorId : String){
        val sp = requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE)
        sp.edit().putString("actor",actorId).apply()
    }

}