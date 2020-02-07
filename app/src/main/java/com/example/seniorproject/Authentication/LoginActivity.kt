package com.example.seniorproject.Authentication
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.content.Intent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.Utils.AuthenticationListener
import com.example.seniorproject.viewModels.AuthenticationViewModel
import com.example.seniorproject.R
//import com.example.seniorproject.Utils.InjectorUtils
import com.example.seniorproject.databinding.ActivityLoginBinding
import com.example.seniorproject.MainForum.MainForum
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), AuthenticationListener {

@Inject
lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: AuthenticationViewModel

    override fun onStarted() {
        }

    override fun onSuccess() {
        val myIntent = Intent(this@LoginActivity, MainForum::class.java)
        this@LoginActivity.startActivity(myIntent)
        //cant redirect to main forum
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

        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProviders.of(this,factory).get(AuthenticationViewModel::class.java)
        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.authViewModel = myViewModel

        myViewModel.authListener = this

    }



}