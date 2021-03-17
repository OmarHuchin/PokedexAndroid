package com.example.pokedex.data.remote

import android.content.Context
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.PokemonResult
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit

class RemoteDataManager(val context: Context) {
    companion object {
        const val SERVER_BASE_URL = "https://pokeapi.co/api/v2/"
        const val SERVER_IMAGE_URL = "https://pokeres.bastionbot.org/images/pokemon/"
        const val NETWORK_LOG_TAG = "OkHttp"
        private const val TIME_OUT:Long = 100
    }

    private val sevice by lazy {
        createService()
    }
    /**
     * OkHttpClient that implements an interceptor
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor{ chain -> run{
            val response = chain.proceed(chain.request())
            if(!response.isSuccessful){
                val source = response.body()!!.source()
                val body = response.body()
                val stringResponse = source.buffer().clone().readString(Charset.defaultCharset()).toString()
            }
            response
        }
        }.addInterceptor { chain ->
            val builder = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
            val request = builder.build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .build()
    /**
     * Creates an instance of the service communication.
     */
    private fun createService(): ApiSevice {
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(SERVER_BASE_URL)
            .build()
        return retrofit.create(ApiSevice::class.java)
    }
    fun getPokemonList(limit: Int, offset:Int): Observable<PokemonResult> = sevice.list(limit,offset)
    fun getPokemonList(url:String): Observable<PokemonResult> = sevice.list(url)
    suspend fun getPokemon(id:Int): Response<Pokemon> =  sevice.getPokemon(id)
    fun getPokemon(url:String): Observable<Pokemon> =  sevice.getPokemon(url)
    fun getPokemonCover(id:Int) = "${RemoteDataManager.SERVER_IMAGE_URL}${id}.png"
    fun getPokemonDetail(id: Int) = "${RemoteDataManager.SERVER_BASE_URL}${id}"

}