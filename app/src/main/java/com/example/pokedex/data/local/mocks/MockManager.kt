package com.example.pokedex.data.local.mocks

import android.content.Context
import com.example.pokedex.data.models.Profile
import com.example.pokedex.data.remote.Requests.LoginRequest

class MockManager(val context: Context) {
    fun getUser():LoginRequest{
        return LoginRequest("omar","123")
    }
    val profile = Profile().apply {
        nickName = "Felix Omar"
    }
}