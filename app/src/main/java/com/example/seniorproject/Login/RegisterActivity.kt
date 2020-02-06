package com.example.seniorproject.Login
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.*
import com.example.seniorproject.Utils.AuthenticationListener
import com.example.seniorproject.viewModels.AuthenticationViewModel
import com.example.seniorproject.databinding.ActivityRegisterBinding
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.Dagger.DaggerAppComponent
import javax.inject.Inject

//import com.example.seniorproject.Utils.InjectorUtils


class RegisterActivity : AppCompatActivity(),
    AuthenticationListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: AuthenticationViewModel

    override fun onStarted() {

    }

    override fun onSuccess() {
        val myIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
        this@RegisterActivity.startActivity(myIntent)
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //initializeUI()
        Log.d("REG","entered register activity")
        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProviders.of(this,factory).get(AuthenticationViewModel::class.java)
        val bindings: ActivityRegisterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_register)
        bindings.authViewModel = myViewModel
        myViewModel.authListener = this

    }



}




