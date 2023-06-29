package com.example.letterboxf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.letterboxf.databinding.ActivityStartBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}