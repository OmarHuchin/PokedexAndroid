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
import io.reactivex.internal.observers.BlockingObserver
import io.reactivex.internal.operators.observable.BlockingObservableIterable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class PokedexViewModel(app: Application): BaseVM<HomeErrorCodes>(app) {
    var offset = 0
    var limit = 20
    var isLoadFromServer: Boolean = false
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
    }
    fun loadData(fromServer:Boolean){
        this.isLoadFromServer = fromServer
        if (isBusy.get() == true) return
        isBusy.set(true)
        if (fromServer){
            load()
        }else{
            GlobalScope.launch {
                loadFromLocal()
            }
        }
    }
    private suspend fun loadFromLocal(){
        val list = repository.readPokemoList()
        currentPokemonList.postValue(ArrayList(list))
        isBusy.set(false)
    }
    private fun load(url: String = ""){
        getPokemonList(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { r-> run {

                    r.results.map {
                        Pokemon(0,it.name,
                            0, null,0,it.url, "","")
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
        if (pokemonResult.value?.results.isNullOrEmpty() || isLoadingMore.get() == true) return
        val result = pokemonResult.value!!.results
        val observables = repository.getPokemons(result.map { it.url })
        var iteration: Int = 0
        Observable.fromIterable(observables)
                .flatMap { it.subscribeOn(Schedulers.io()) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ p ->
                    run {
                        val index = currentPokemonList.value?.indexOfFirst { it.name == p.name } ?: return@run
                        p.url = repository.getPokemonDetail(p.id)
                        p.coverImage = repository.getPokemonCoverImage(p.id)
                        p.frontImage = p.sprites?.front_default ?: ""
                        if (index < 0) return@run
                        iteration += 1
                        val list = ArrayList(currentPokemonList.value)
                        list[index] = p
                        currentPokemonList.value = list
                        save(list)
                        if (iteration >= (list.size - 1))
                        {
                            isLoadingMore.set(false)
                        }
                    }
                }, ::handleErrorCall)
                .addToDisposables(compositeDisposable)
    }
    fun save(pokemons:List<Pokemon>){
        GlobalScope.launch {
            repository.insertPokemonList(pokemons)
        }
    }
    fun loadMore(){
        if(isLoadingMore.get() == true || !isLoadFromServer) return
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