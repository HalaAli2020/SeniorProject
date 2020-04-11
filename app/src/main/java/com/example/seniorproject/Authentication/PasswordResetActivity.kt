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
//import com.example.seniorproject.MainForum.MainForum
import com.example.seniorproject.R
import com.example.seniorproject.Utils.AuthenticationListener
import com.example.seniorproject.databinding.ActivityPasswordResetBinding
import com.example.seniorproject.viewModels.AuthenticationViewModel
import javax.inject.Inject


class PasswordResetActivity : AppCompatActivity(), AuthenticationListener {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: AuthenticationViewModel

    override fun onStarted() {
    }

    override fun onSuccess() {
        val myIntent = Intent(this@PasswordResetActivity, LoginActivity::class.java)
        this@PasswordResetActivity.startActivity(myIntent)
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    // User field to pass the to next fragment
    private lateinit var user: Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)
        Log.d("TAG","test logcat")

        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProvider(this,factory).get(AuthenticationViewModel::class.java)
        val binding: ActivityPasswordResetBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_password_reset)
        binding.authViewModel = myViewModel

        myViewModel.authListener = this

    }
}