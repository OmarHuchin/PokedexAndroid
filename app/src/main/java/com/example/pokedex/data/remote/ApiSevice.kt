package com.example.pokedex.data.remote

import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.PokemonResult
import com.example.pokedex.data.remote.Requests.LoginRequest
import io.reactivex.Observable
import retrofit2.http.*

interface ApiSevice {
    @POST("account/authenticate")
    fun authenticate(@Body request: LoginRequest): Observable<LoginRequest>
    @GET("pokemon")
    fun list(@Query("limit") limit: Int, @Query("offset") offset:Int): Observable<PokemonResult>
    @GET
    fun list(@Url url: String): Observable<PokemonResult>
    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id: Int):Observable<Pokemon>
    @GET
    fun getPokemon(@Url url: String):Observable<Pokemon>
}