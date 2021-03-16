package com.example.pokedex.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.pokedex.data.Repository
import com.example.pokedex.utils.LoginErrorCodes
import kotlinx.coroutines.launch

import javax.xml.validation.Validator

/*
* class LoginVMw(application: Application) : BaseVM<LoginErrorCodes>(application) {
    // Create a LiveData with a String
    val userName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val password: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val isUserNameValid: LiveData<Boolean> = Transformations.map(userName) {
        !it.isNullOrEmpty()
    }
    val userNameError: LiveData<String> = Transformations.map(isUserNameValid) {
        when (it) {
            true -> ""
            false -> "Must contain only letters numbers and underscores" // It should be get from R.string
        }
    }
    fun login() = viewModelScope.launch {
        performRepositoryCall()
    }
    override fun firstRepositoryCallDefinition() {
       Log.e("TAG","REPO")
    }

    override fun areValidFields(): Boolean {
        var isValid = true
        var uvalid = isUserNameValid.value
        if (userName.value.isNullOrEmpty()){
            isValid = false
        }
        if(password.value.isNullOrEmpty()){
            isValid = false
        }
        return isValid
    }
}
* */
