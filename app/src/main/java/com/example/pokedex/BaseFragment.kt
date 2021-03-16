package com.example.pokedex

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.pokedex.views.CustomAlertDialog

open class BaseFragment: Fragment(){
    /**
     * Creates an [AlertDialog] formatted from a message [String]
     *
     * @param context The context where the method is called
     * @param message The [String] of message
     *
     * @return An [CustomAlertDialog] ready to show
     */
    fun createAlert(context: Context, title:String, message: String): CustomAlertDialog {
        val alert = CustomAlertDialog(context)
        alert.title = title
        alert.message = message
        return alert
    }
}