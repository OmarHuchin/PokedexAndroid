package com.example.pokedex

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.example.pokedex.utils.DataErrorCodes
import com.example.pokedex.utils.observe
import com.example.pokedex.viewmodels.BaseVM
import com.example.pokedex.viewmodels.HomeViewModel
import com.example.pokedex.viewmodels.LoginVM
import com.example.pokedex.views.CustomAlertDialog
import retrofit2.HttpException
import java.lang.Exception

abstract class BaseCompactActivity<BindType: ViewDataBinding,ErrorType, VMType: BaseVM<ErrorType>> : AppCompatActivity() {
    protected val binding: BindType by lazy{
        initBinding()
    }
    protected val viewModel: VMType by lazy{
        initViewModel()
    }
    abstract fun initViewModel(): VMType
    abstract fun initBinding(): BindType
    abstract fun setUpUI()
    abstract fun setListeners()
    abstract fun onValidationError(errors: List<ErrorType>)
    /**
     * Function needed for setting the default observers for the LiveData changes
     */
    protected open fun initObservers() {
        /**
         * Error observer
         */
        observe(viewModel.errorOnData()) { error ->
            onErrorData(error)
        }
        /**
         * Error observer
         */
        observe(viewModel.exceptionOnData()) { error ->
            onExceptionData(error)
        }

        /**
         * Error observer
         */
        observe(viewModel.errorOnNetworkData()) { error ->
            onExceptionData(error)
        }

        /**
         * Success observer
         */
        observe(viewModel.successOnData()) { result ->
            onSuccessNetworkData(result)
        }
    }
    /**
     * Base error handling logic
     *
     * Note: override if need a custom error handling
     */
    open fun onErrorData(error: DataErrorCodes?){
        createErrorAlert(this, error)
    }
    /**
     * Base error handling logic
     *
     * Note: override if need a custom error handling
     */
    open fun onExceptionData(exception: Exception?){
        createErrorAlert(this, null,exception)
    }
    /**
     * Base error handling logic
     *
     * Note: override if need a custom error handling
     */
    abstract fun onSuccessNetworkData(data: Any?)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val vmType = viewModel is HomeViewModel
        Log.e("Val", vmType.toString())
        setListeners()
        initObservers()
        viewModel.errorOnInput().observe(this,  { errors ->
            onValidationError(errors)
        })
    }
    /**
     * Sets the listeners for clearing the errors if the focus is lost
     */
    protected fun performRepositoryCall(view: View? = null){
        if (view != null) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(view.windowToken, 0)
        }
        viewModel.performRepositoryCall()
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
            is HttpException->{
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