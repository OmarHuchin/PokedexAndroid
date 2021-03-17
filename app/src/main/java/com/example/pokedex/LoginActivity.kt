package com.example.pokedex

import android.os.Bundle
import android.util.Log
import com.example.pokedex.databinding.ActivityLoginBinding
import com.example.pokedex.utils.LoginErrorCodes
import androidx.activity.viewModels
import com.example.pokedex.utils.startActivityNewRoot
import com.example.pokedex.viewmodels.LoginVM
class LoginActivity :BaseCompactActivity<ActivityLoginBinding,LoginErrorCodes,LoginVM>(){

    override fun initBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun initViewModel(): LoginVM {
         val model : LoginVM by viewModels()
        binding.viewModel = model
        binding.lifecycleOwner = this
        model.userName.observe(this){
            Log.e("Tag",it)
        }
        return model
    }
    override fun setUpUI() {

    }
    override fun setListeners() {
        binding.loginBtn.setOnClickListener {
            performRepositoryCall(it)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(viewModel.isLogged())
            onSuccessNetworkData(null)
    }
    override fun onSuccessNetworkData(data: Any?) {
        startActivityNewRoot<HomeActivity> {  }
    }
    override fun onValidationError(errors: List<LoginErrorCodes>) {
        binding.userName.error = null
        binding.userPassword.error = null
        if (errors.contains(LoginErrorCodes.EMPTY_USER)){
            binding.userName.error = getString(R.string.error_login_blank_user)
        }
        if (errors.contains(LoginErrorCodes.EMPTY_PASSWORD)){
            binding.userPassword.error = getString(R.string.error_login_blank_password)
        }
    }
}
