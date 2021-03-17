package com.example.pokedex.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.pokedex.R
import com.example.pokedex.data.models.Profile
import com.example.pokedex.utils.LoginErrorCodes
import com.example.pokedex.utils.addToDisposables
import com.example.pokedex.utils.isValidEmail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class LoginVM(application: Application) : BaseVM<LoginErrorCodes>(application) {
    // Create a LiveData with a String
    val userName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val password: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val isUserNameValid: LiveData<Boolean> = Transformations.map(userName) {
        it.isValidEmail()
    }
    private val isPasswordValid: LiveData<Boolean> = Transformations.map(password) {
        !it.isNullOrEmpty()
    }
    val userNameError: LiveData<String> = Transformations.map(isUserNameValid) {
        when (it) {
            true -> ""
            false -> context.getString(R.string.error_login_blank_user) // It should be get from R.string
        }
    }
    val passwordError: LiveData<String> = Transformations.map(isPasswordValid) {
        when (it) {
            true -> ""
            false -> context.getString(R.string.error_login_blank_password) // It should be get from R.string
        }
    }
    fun login() = viewModelScope.launch {
        performRepositoryCall()
    }
    fun isLogged():Boolean{
        return repository.isLogged()
    }
    override fun areValidFields(): Boolean {
        var isValid = true
        if (userName.value.isNullOrEmpty() || !userName.value.isValidEmail()){
            isValid = false
        }
        if(password.value.isNullOrEmpty()){
            isValid = false
        }
        return isValid
    }
    override fun firstRepositoryCallDefinition() {
        repository.login(userName.value ?: "",password.value ?: "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->  run{
                    isBusy.set(false)
                    handleSuccessCall(result)
                    if(result is Profile)
                        repository.saveProfile(result)
                }},
                ::handleErrorCall
            ).addToDisposables(compositeDisposable)
    }

    override fun secondRepositoryCallDefinition() {
        TODO("Not yet implemented")
    }

    override fun onException(error: Throwable) {

    }
}
