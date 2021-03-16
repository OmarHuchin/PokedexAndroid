package com.example.pokedex.utils

import androidx.annotation.StringRes
import com.example.pokedex.R

enum class ProfileErrorCodes (@StringRes private val resourceId: Int) : ErrorEvent {
    NONE(0),
    EMPTY_LIST(R.string.pokemons_are_not_avaialables);
    override fun getErrorResource(): Int = resourceId
}