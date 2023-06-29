package com.example.letterboxf.ui.viewpagerFragments

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.letterboxf.adapter.recyclerView.forTextViews.TextViewAdapter
import com.example.letterboxf.base.BaseFragment
import com.example.letterboxf.databinding.FragmentDetailViewPagerBinding
import com.example.letterboxf.model.custom.TextData
import com.example.letterboxf.viewmodel.SameForAllViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailViewPagerFragment : BaseFragment<FragmentDetailViewPagerBinding>(FragmentDetailViewPagerBinding::inflate) {

    private val sameForAllViewModel: SameForAllViewModel by viewModels()
    private val genreAdapter = TextViewAdapter()
    private val companyAdapter = TextViewAdapter()
    private val countryAdapter = TextViewAdapter()
    private val languageAdapter = TextViewAdapter()

    override fun onViewCreatedLight() {

        observeViewModel()
        setRv()
    }

    private fun setRv(){
        binding.rvCompanies.adapter = companyAdapter
        binding.rvCountries.adapter = countryAdapter
        binding.rvLanguages.adapter = languageAdapter
        binding.rvGenres.adapter = genreAdapter
    }


    private fun observeViewModel(){
        val textData = ArrayList<TextData>()
        with(sameForAllViewModel){
            getMovieDetails(getSp())
            movieDetails.observe(viewLifecycleOwner){
                if (it!=null){
                    textData.clear()
                    binding.textViewBudget.text = "$${it.budget}"
                    it.genres.forEach {genre->
                        textData.add(TextData(genre.name,genre.id.toLong()))
                        Log.e("COWBOY FROM DETAILS:",textData.toString())
                    };genreAdapter.updateAdapter(textData)
                    textData.clear()
                    it.productionCompanies.forEach {company->
                        textData.add(TextData(company.name,company.id.toLong()))
                    };companyAdapter.updateAdapter(textData)
                    textData.clear()
                    it.productionCountries.forEach {country->
                        textData.add(TextData(country.name,country.iso31661.length.toLong()))
                    };countryAdapter.updateAdapter(textData)
                    textData.clear()
                    it.spokenLanguages.forEach {language->
                        textData.add(TextData(language.name,language.iso6391.length.toLong()))
                    };languageAdapter.updateAdapter(textData)
                }else{
                    Toast.makeText(context,"Details of the film are not available", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun getSp() : String{
        return requireActivity().getSharedPreferences("id", Context.MODE_PRIVATE).getString("movie","0")!!
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}