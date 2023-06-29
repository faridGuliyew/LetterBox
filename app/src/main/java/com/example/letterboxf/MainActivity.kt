package com.example.letterboxf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.letterboxf.databinding.ActivityMainBinding
import com.example.letterboxf.viewmodel.PopularViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //Make the request in advance
    private val popularViewModel : PopularViewModel by viewModels()
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        popularViewModel.getPopularReviews(1)
    }
}