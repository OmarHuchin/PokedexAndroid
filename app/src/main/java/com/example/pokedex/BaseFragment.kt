package com.example.pokedex

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.pokedex.utils.DataErrorCodes
import com.example.pokedex.views.CustomAlertDialog
import retrofit2.HttpException
import java.lang.Exception

open class BaseFragment: Fragment(){
    /**
     * Base error handling logic
     *
     * Note: override if need a custom error handling
     */
    open fun onErrorData(error: DataErrorCodes?){
        createErrorAlert(requireContext(), error)
    }
    /**
     * Base error handling logic
     *
     * Note: override if need a custom error handling
     */
    open fun onExceptionData(exception: Exception?){
        createErrorAlert(requireContext(), null,exception)
    }
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
    /**
     * Evaluates [DataErrorCodes] and [HttpException] and Creates and Show an [AlertDialog]
     *
     * @param context The context where the method is called
     * @param error The [DataErrorCodes] who determine the message
     * @param exception The [HttpException] with the information
     */
    fun createErrorAlert(context: Context, error: DataErrorCodes? = null, exception: Exception? = null) {
        if (error != null){
            createErrorAlertFromCode(context, error).show()
        }else {
            createErrorAlertFromException(context, exception).show()
        }
    }
    /**
     * Creates an [AlertDialog] formatted from an [DataErrorCodes]
     *
     * @param context The context where the method is called
     * @param errorCode The [DataErrorCodes] who determine the message
     *
     * @return An [CustomAlertDialog] ready to show
     */
    private fun createErrorAlertFromCode(context: Context, errorCode: DataErrorCodes): CustomAlertDialog{
        return createErrorAlert(context, context.getString(errorCode.getErrorResource()))
    }
    /**
     * Creates an [AlertDialog] formatted from an [HttpException]
     *
     * @param context The context where the method is called
     * @param exception The [HttpException] who determine the message
     *
     * @return An [CustomAlertDialog] ready to show
     */
    private fun createErrorAlertFromException(context: Context, exception: Exception?): CustomAlertDialog{
        val message = when(exception){
            is HttpException ->{
                when(exception?.code()){
                    400 -> context.getString(R.string.message_error_400)
                    else ->context.getString(R.string.message_error_general)
                }
            }
            else ->{
                exception?.message ?: context.getString(R.string.message_error_general)
            }
        }
        return createErrorAlert(context, message)
    }
    /**
     * Creates an [AlertDialog] formatted from a message [String]
     *
     * @param context The context where the method is called
     * @param message The [String] of message
     *
     * @return An [CustomAlertDialog] ready to show
     */
    private fun createErrorAlert(context: Context, message: String): CustomAlertDialog {
        val alert = CustomAlertDialog(context)
        alert.title = context.getString(R.string.title_alert_error)
        alert.message = message
        alert.setNeutralButton(context.getString(R.string.button_accept)){}
        return alert
    }
}