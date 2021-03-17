package com.example.pokedex.data.remote

import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.PokemonResult
import com.example.pokedex.data.remote.Requests.LoginRequest
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface ApiSevice {

    @GET("pokemon")
    fun list(@Query("limit") limit: Int, @Query("offset") offset:Int): Observable<PokemonResult>
    @GET
    fun list(@Url url: String): Observable<PokemonResult>
    @GET
    suspend fun fetchList(@Url url: String): Response<PokemonResult>
    @GET
    suspend fun fetchPokemon(@Url url: String):Response<Pokemon>
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int):Response<Pokemon>
    @GET
    fun getPokemon(@Url url: String):Observable<Pokemon>
}