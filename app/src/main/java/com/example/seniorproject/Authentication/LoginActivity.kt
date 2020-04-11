package com.example.seniorproject.Authentication
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.MainForum
import com.example.seniorproject.R
import com.example.seniorproject.Utils.AuthenticationListener
import com.example.seniorproject.databinding.ActivityLoginBinding
import com.example.seniorproject.viewModels.AuthenticationViewModel
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), AuthenticationListener {

@Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: AuthenticationViewModel

    override fun onStarted() {
        }

    override fun onSuccess() {
        //called in Firebase Data Login User function navigates to mainforum page
        val myIntent = Intent(this@LoginActivity, MainForum::class.java)
        this@LoginActivity.startActivity(myIntent)
        Toast.makeText(this, "Successfully logged in!", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // User field to pass the to next fragment
    private lateinit var user: Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d("TAG","test logcat")

        //initalization of the viewmodel
        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProvider(this,factory).get(AuthenticationViewModel::class.java)
        //initialization of binding variable, binded variables are located in the corresponding XML file
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.authViewModel = myViewModel
        myViewModel.authListener = this

    }



}
