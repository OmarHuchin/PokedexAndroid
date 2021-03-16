package com.example.pokedex.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.data.models.PokemonResult
import com.example.pokedex.utils.HomeErrorCodes
import com.example.pokedex.utils.addToDisposables
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel(app: Application) : BaseVM<HomeErrorCodes>(app) {
    var offset = 0;
    var limit = 20;
    val pokemonResult: MutableLiveData<PokemonResult> by lazy {
        MutableLiveData<PokemonResult>()
    }
    override fun firstRepositoryCallDefinition() {
        isBusy.set(true)
        repository.getPokemonList(limit,offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { r-> run {
                    isBusy.set(false)
                    handleSuccessCall(r)
                } },
                ::handleErrorCall
            ).addToDisposables(compositeDisposable)
    }

    override fun areValidFields(): Boolean = true
    override fun secondRepositoryCallDefinition() {

    }

    override fun onException(error: Throwable) {
        TODO("Not yet implemented")
    }

}