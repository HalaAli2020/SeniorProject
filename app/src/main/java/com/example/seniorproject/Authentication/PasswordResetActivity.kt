package com.example.seniorproject.Authentication
//import com.example.seniorproject.MainForum.MainForum
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.Dagger.DaggerAppComponent
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
        //success originates from response of Firebase Data Reset Password function navigates to Login page
        val myIntent = Intent(this@PasswordResetActivity, LoginActivity::class.java)
        this@PasswordResetActivity.startActivity(myIntent)
    }

    //shows the error toast message that is retrieved from error code message in built in Firebase Reset Password function
    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        //initalization of the viewmodel
        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProvider(this,factory).get(AuthenticationViewModel::class.java)

        //initialization of binding variable, binded variables are located in the corresponding XML file
        val binding: ActivityPasswordResetBinding = DataBindingUtil.setContentView(this, R.layout.activity_password_reset)
        binding.authViewModel = myViewModel

        myViewModel.authListener = this

    }
}