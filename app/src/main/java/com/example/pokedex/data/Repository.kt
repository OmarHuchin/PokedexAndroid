package com.example.pokedex.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.Transformations
import com.example.pokedex.data.local.PreferencesManager
import com.example.pokedex.data.local.mocks.MockManager
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.PokemonResult
import com.example.pokedex.data.models.Profile
import com.example.pokedex.data.models.User
import com.example.pokedex.data.remote.RemoteDataManager
import com.example.pokedex.data.remote.Requests.LoginRequest
import com.example.pokedex.utils.JsonHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit

class Repository(val context:Context) {
    private val remoteDataManager by lazy {
        RemoteDataManager(context)
    }
    private val preferencesManager by lazy {
        PreferencesManager(context)
    }
    private val mockManager by lazy {
        MockManager(context)
    }
    fun login(user: String, password: String) : Observable<Profile> {
        val localUser = mockManager.getUser()
        val users = getUsersFromJson()
        val user = users.find { it.email == user && it.password == password }
        if (user != null){
            return Observable.just(Profile().apply {
                nickName = user.nickName
            })
                .delay(5,TimeUnit.SECONDS)
        }
        return Observable.error<Profile>(Exception("Invalid user"))
    }
    fun getProfile():Profile{
        return preferencesManager.currentUser
    }
    fun saveProfile(profile: Profile){
        preferencesManager.currentUser = profile
        preferencesManager.isLogged = true
    }
    fun deleteProfile(){
        preferencesManager.currentUser = Profile().apply {
            nickName = ""
        }
        preferencesManager.isLogged = false
    }
    fun isLogged():Boolean{
        return preferencesManager.isLogged
    }
    fun getPokemonList(limit:Int, offset:Int): Observable<PokemonResult> = remoteDataManager.getPokemonList(limit,offset)
    fun getPokemonList(url: String): Observable<PokemonResult> = remoteDataManager.getPokemonList(url)
    fun getPokemon(id:Int): Observable<Pokemon> = remoteDataManager.getPokemon(id)
    fun getPokemon(url:String): Observable<Pokemon> = remoteDataManager.getPokemon(url)
    fun getPokemons(list: List<String>):List<Observable<Pokemon>> = list.map {
        getPokemon(url = it)
    }
    private fun getUsersFromJson():List<User>{
        val jsonFileString = JsonHelper.getJsonDataFromAsset(context, "users.json")
        val gson = Gson()
        val listPersonType = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(jsonFileString, listPersonType)
    }
}