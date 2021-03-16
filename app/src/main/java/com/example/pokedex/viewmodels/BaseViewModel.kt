package com.example.pokedex.viewmodels

import android.app.Application
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.Repository
import com.example.pokedex.utils.DataErrorCodes
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
abstract class BaseVM<ErrorType>(app: Application) : AndroidViewModel(app){
    protected  val context: Context by lazy { getApplication<Application>().applicationContext }
    protected val repository by lazy {
        Repository(context)
    }
    val isBusy = ObservableField<Boolean>(false)
    var compositeDisposable = CompositeDisposable()

    var validationAttemptFailed = false

    fun performRepositoryCall(silent: Boolean = false){
        isBusy.set(!silent)
        if (areValidFields()){
            firstRepositoryCallDefinition()
        }else isBusy.set(false)
    }
    /**
     * Executes the [Repository] call
     */
    abstract fun firstRepositoryCallDefinition()
    /**
     * Executes the [Repository] call
     */
    abstract fun secondRepositoryCallDefinition()
    /**
     * Applies business logic to the required fields
     *
     * @return true if the fields are valid, false otherwise.
     */
    abstract fun areValidFields(): Boolean
    abstract fun onException(error: Throwable)
    /**
     * LiveData for input error handling
     */
    protected val errorOnInput = MutableLiveData<List<ErrorType>>()
    fun errorOnInput(): LiveData<List<ErrorType>> {
        return errorOnInput
    }

    /**
     * LiveData for data error handling
     */
    private val errorOnData = MutableLiveData<DataErrorCodes>()
    fun errorOnData(): LiveData<DataErrorCodes> {
        return errorOnData
    }
    /**
     * LiveData for exception handling
     */
    private val exceptionOnData = MutableLiveData<Exception>()
    fun exceptionOnData(): LiveData<Exception> {
        return exceptionOnData
    }
    /**
     * LiveData for network error handling
     */
    private val errorOnNetworkData = MutableLiveData<HttpException>()
    fun errorOnNetworkData(): LiveData<HttpException> {
        return errorOnNetworkData
    }

    /**
     * LiveData for success server call handling
     */
    private val successOnData = MutableLiveData<Any>()
    fun successOnData(): LiveData<Any> {
        return successOnData
    }

    /**
     * Handles the success server call
     *
     * Note: This function needs an observer for **successCall()** LiveData for handle a successful process.
     */
    protected open fun handleSuccessCall(result: Any){
        isBusy.set(false)
        successOnData.value = result
    }

    protected fun handleErrorCall(error: Throwable) {
        isBusy.set(false)
        when (error) {
            is HttpException -> {
                errorOnNetworkData.value = error
            }
            is UnknownHostException -> {
                errorOnData.value = DataErrorCodes.CONNECTION_ERROR
            }
            is ConnectException -> {
                errorOnData.value = DataErrorCodes.SERVER_CONNECTION_ERROR
            }
            is SocketTimeoutException -> {
                errorOnData.value = DataErrorCodes.TIMEOUT_ERROR
            }
            is Exception->{
                exceptionOnData.value = error;
            }
            else -> {
                Log.e("OkHttp", "ERROR: " + error.message)
                errorOnData.value = DataErrorCodes.NONE
            }
        }
        onException(error)
    }
    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}