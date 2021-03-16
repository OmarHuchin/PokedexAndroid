package com.example.pokedex.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import com.example.pokedex.databinding.CustomAlertLayoutBinding

/**
 * Custom Alert Dialog for standardize the alerts in the app.
 */
class CustomAlertDialog(context: Context) : Dialog(context) {

    var title: String? = null
    var message: String? = null

    private lateinit var binding: CustomAlertLayoutBinding

    private var positiveText: String? = null
    private var positiveListener: View.OnClickListener? = null
    private var negativeText: String? = null
    private var negativeListener: View.OnClickListener? = null
    private var neutralText: String? = null
    private var neutralListener: View.OnClickListener? = null
    private var showPositive = false
    private var showNegative = false
    private var showNeutral = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CustomAlertLayoutBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.alertTitle.text = title
        binding.alertMessage.text = message
        setListeners()
    }

    /**
     * Set the listeners to the buttons of the dialog.
     */
    private fun setListeners(){
        if (showPositive){
            binding.buttonPositive.visibility = View.VISIBLE
            binding.buttonPositive.text = positiveText
            binding.buttonPositive.setOnClickListener {
                positiveListener?.onClick(it)
                dismiss()
            }
        }
        if (showNegative){
            binding.buttonNegative.visibility = View.VISIBLE
            binding.buttonNegative.text = negativeText
            binding.buttonNegative.setOnClickListener {
                negativeListener?.onClick(it)
                dismiss()
            }
        }
        if (showNeutral){
            binding.buttonNeutral.visibility = View.VISIBLE
            binding.buttonNeutral.text = neutralText
            binding.buttonNeutral.setOnClickListener {
                neutralListener?.onClick(it)
                dismiss()
            }
        }
    }

    /**
     * Set a listener to be invoked when the positive button of the dialog is pressed.
     */
    fun setPositiveButton(text: String, onClickListener: View.OnClickListener) {
        showPositive = true
        positiveListener = onClickListener
        positiveText = text
    }
    /**
     * Set a listener to be invoked when the negative button of the dialog is pressed.
     */
    fun setNegativeButton(text: String, onClickListener: View.OnClickListener) {
        showNegative = true
        negativeListener = onClickListener
        negativeText = text
    }

    /**
     * Set a listener to be invoked when the neutral button of the dialog is pressed.
     */
    fun setNeutralButton(text: String, onClickListener: View.OnClickListener) {
        showNeutral = true
        neutralListener = onClickListener
        neutralText = text
    }
}