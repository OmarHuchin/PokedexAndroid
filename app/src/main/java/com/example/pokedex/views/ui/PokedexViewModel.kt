package com.example.pokedex.views.ui

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.navigation.fragment.NavHostFragment
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.data.models.PokemonResult
import com.example.pokedex.data.models.Sprites
import com.example.pokedex.utils.HomeErrorCodes
import com.example.pokedex.utils.addToDisposables
import com.example.pokedex.viewmodels.BaseVM
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class PokedexViewModel(app: Application): BaseVM<HomeErrorCodes>(app) {
    var offset = 0;
    var limit = 20;
    val pokemonResult: MutableLiveData<PokemonResult> by lazy {
        MutableLiveData<PokemonResult>()
    }
    val currentPokemonList: MutableLiveData<ArrayList<Pokemon>> by lazy{
        MutableLiveData()
    }
    val isLoadingMore: ObservableField<Boolean> by lazy{
        ObservableField()
    }
    override fun firstRepositoryCallDefinition() {
        isBusy.set(true)
        load()
    }
    private fun load(url: String = ""){
        getPokemonList(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { r-> run {

                    r.results.map {
                        Pokemon(0,"",
                            0, null,0)
                    }.also { p->
                        if (url.isNullOrEmpty()) {
                            pokemonResult.value = r
                            currentPokemonList.value = ArrayList(p)
                        }
                        else
                        {
                            val oldList = pokemonResult.value?.results ?: return@run
                            val newList = r.results
                            r.results = oldList.plus(newList)
                            pokemonResult.value = r
                            currentPokemonList.value?.addAll(ArrayList(p))
                        }
                    }
                    isBusy.set(false)
                    isLoadingMore.set(false)
                    secondRepositoryCallDefinition()
                } },
                ::handleErrorCall
            ).addToDisposables(compositeDisposable)
    }
    private fun getPokemonList(url: String = ""):Observable<PokemonResult> =  when(url){
        ""-> repository.getPokemonList(limit,offset)
        else->repository.getPokemonList(url)
    }

    override fun areValidFields(): Boolean = true
    override fun secondRepositoryCallDefinition() {

        if (pokemonResult.value?.results.isNullOrEmpty()) return
        val result = pokemonResult.value!!.results
        val r = result.filter { !it.url.isNullOrEmpty() }.map {
            repository.getPokemon(it.url ?: "")
        }
        r.forEach {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    r-> run {
                        val index = result.indexOfFirst { it.name == r.name }
                        currentPokemonList.value!![index] = r
                        handleSuccessCall(Pair(index,r))
                        isLoadingMore.set(false)
                    }
                    }, ::handleErrorCall)
                .addToDisposables(compositeDisposable)
        }
    }
    fun loadMore(){
        if(isLoadingMore.get() == true) return
        isLoadingMore.set(true)
        val nextFetch = pokemonResult.value?.next ?: return
        load(nextFetch)
    }
    fun showProfile(){

    }
    override fun onException(error: Throwable) {
        isLoadingMore.set(false)
    }
}