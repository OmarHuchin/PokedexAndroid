package com.example.pokedex.utils

import androidx.annotation.StringRes
import com.example.pokedex.R

enum class DataErrorCodes (@StringRes private val resourceId: Int) : ErrorEvent {
    NONE(0),
    NETWORK_ERROR(R.string.message_error_connection),
    CONNECTION_ERROR(R.string.message_error_connection),
    SERVER_CONNECTION_ERROR(R.string.message_error_server_connection),
    TIMEOUT_ERROR(R.string.message_error_timeout);
    override fun getErrorResource() = resourceId
}
interface ErrorEvent {
    @StringRes
    fun getErrorResource(): Int
}