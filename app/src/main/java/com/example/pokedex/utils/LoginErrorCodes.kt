package com.example.pokedex.utils

import androidx.annotation.StringRes
import com.example.pokedex.R

enum class LoginErrorCodes(@StringRes private val resourceId: Int) : ErrorEvent {
    NONE(0),
    EMPTY_USER(R.string.error_login_blank_user),
    EMPTY_PASSWORD(R.string.error_login_blank_password);
    override fun getErrorResource() = resourceId
}
