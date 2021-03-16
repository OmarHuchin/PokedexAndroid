package com.example.pokedex.data

import android.content.Context
import com.example.pokedex.data.local.PreferencesManager
import com.example.pokedex.data.local.mocks.MockManager
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.PokemonResult
import com.example.pokedex.data.models.Profile
import com.example.pokedex.data.remote.RemoteDataManager
import com.example.pokedex.data.remote.Requests.LoginRequest
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
        if(localUser.equals(LoginRequest(user,password))){
            return Observable.just(mockManager.profile)
                .delay(5,TimeUnit.SECONDS)
        }
        return Observable.error<Profile>(Exception("Invalid user"))
    }
    fun saveProfile(profile: Profile){
        preferencesManager.currentUser = profile
        preferencesManager.isLogged = true
    }
    fun isLogged():Boolean{
        return preferencesManager.isLogged
    }
    fun getPokemonList(limit:Int, offset:Int): Observable<PokemonResult> = remoteDataManager.getPokemonList(limit,offset)
    fun getPokemonList(url: String): Observable<PokemonResult> = remoteDataManager.getPokemonList(url)
    /*suspend fun getPokemons(list: List<String>)= flow{
        val pokes = list.flatMap {
            remoteDataManager.getPokemon(it)
        }
    }*/
    fun getPokemon(id:Int): Observable<Pokemon> = remoteDataManager.getPokemon(id)
    fun getPokemon(url:String): Observable<Pokemon> = remoteDataManager.getPokemon(url)
}