package com.example.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.pokedex.databinding.ActivityHomeBinding
import com.example.pokedex.utils.HomeErrorCodes
import com.example.pokedex.viewmodels.HomeViewModel

class HomeActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()
    }
}