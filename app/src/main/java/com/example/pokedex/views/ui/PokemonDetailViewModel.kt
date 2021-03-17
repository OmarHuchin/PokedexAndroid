package com.example.pokedex.views.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.local.database.entities.PokemonEntity
import com.example.pokedex.data.models.Pokemon
import com.example.pokedex.utils.HomeErrorCodes
import com.example.pokedex.viewmodels.BaseVM
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.lang.Exception

class PokemonDetailViewModel(app: Application) : BaseVM<HomeErrorCodes>(app) {
    lateinit var pokemon: LiveData<Pokemon>

    override fun firstRepositoryCallDefinition() {

    }
    override fun secondRepositoryCallDefinition() {
        TODO("Not yet implemented")
    }
    override fun areValidFields(): Boolean = true
    private val pokemonDetail = MutableLiveData<Pokemon>()

    override fun onException(error: Throwable) {
    }
    fun load(id:Int){
        viewModelScope.launch {
            pokemon = repository.getPokemonFlow(id)
                .catch { handleErrorCall(it) }
                .asLiveData(coroutineContext)
            pokemon.let {
                if (it.value != null)
                    handleSuccessCall(pokemon.value!!)
            }
        }
    }
}