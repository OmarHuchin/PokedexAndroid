package com.example.pokedex.views.ui

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.pokedex.data.models.Profile
import com.example.pokedex.utils.ProfileErrorCodes
import com.example.pokedex.viewmodels.BaseVM

class ProfileViewModel(app: Application):BaseVM<ProfileErrorCodes>(app) {
    val profile: MutableLiveData<Profile> by lazy {
        MutableLiveData()
    }
    var userName: LiveData<String> = Transformations.map(profile){
        it.nickName
    }
    override fun firstRepositoryCallDefinition() {
        TODO("Not yet implemented")
    }

    override fun secondRepositoryCallDefinition() {
        TODO("Not yet implemented")
    }

    override fun areValidFields(): Boolean = true

    override fun onException(error: Throwable) {
        TODO("Not yet implemented")
    }

}