package com.example.seniorproject.Authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.*
import com.example.seniorproject.AuthenticationListener
import com.example.seniorproject.utils.startLoginActivity
import com.example.seniorproject.viewModels.AuthenticationViewModel
import com.example.seniorproject.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity(), AuthenticationListener {


    override fun onStarted() {

    }

    override fun onSuccess() {
        startLoginActivity()
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initializeUI()
        Log.d("REG","entered register activity")

    }



    private fun initializeUI(){

        val factory = InjectorUtils.provideAuthViewModelFactory()

        val binding: ActivityRegisterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_register)
        var viewModel: AuthenticationViewModel = ViewModelProviders.of(this, factory).get(
            AuthenticationViewModel::class.java)

        binding.authViewModel = viewModel

        viewModel.authListener = this

    }


}




