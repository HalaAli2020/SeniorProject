package com.example.seniorproject.Authentication
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.R
import com.example.seniorproject.Utils.AuthenticationListener
import com.example.seniorproject.databinding.ActivityRegisterBinding
import com.example.seniorproject.viewModels.AuthenticationViewModel
import javax.inject.Inject


class RegisterActivity : AppCompatActivity(),
    AuthenticationListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: AuthenticationViewModel

    override fun onStarted() {

    }

    override fun onSuccess() {
        //originates from response of Firebase Data Register function navigates to log in page
        Toast.makeText(this, "A verification email has been sent, please verify before you log in", Toast.LENGTH_SHORT).show()
        val myIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
        this@RegisterActivity.startActivity(myIntent)
    }

    //shows the error toast message that is retrieved from error code message in built in Firebase Register function
    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //users can only see where they're typing with this mode.
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        //initalization of the viewmodel and dagger app component
        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProvider(this,factory).get(AuthenticationViewModel::class.java)

        //initialization of binding variable, binded variables are located in the corresponding XML file
        val bindings: ActivityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        bindings.authViewModel = myViewModel
        myViewModel.authListener = this

    }

}




