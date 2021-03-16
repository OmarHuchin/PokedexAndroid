package com.example.pokedex.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.pokedex.data.models.Profile

class PreferencesManager(context: Context)  {
    companion object {
        //Device keys
        const val IsLogged = "IsLogged"
        const val NickName = "NickName"
    }
    private val filename = "pokedexPreferences"
    private val prefs: SharedPreferences = context.getSharedPreferences(filename, 0)

    /**
     * Flag to determine the active language.
     */
    var isLogged: Boolean
        get() = prefs.getBoolean(IsLogged, false)
        set(value) = prefs.edit().putBoolean(IsLogged, value).apply()

    var currentUser: Profile
    get() = Profile().apply {
        nickName = prefs.getString(NickName,"") ?: ""
    }
    set(value) {
        prefs.edit().putString(NickName,value.nickName).apply()
    }
}