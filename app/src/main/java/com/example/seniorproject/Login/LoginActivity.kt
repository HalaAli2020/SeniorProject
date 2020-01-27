package com.example.seniorproject.Login


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.seniorproject.R
import com.example.seniorproject.MainForum.MainForum
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import com.example.seniorproject.model.User
import com.google.firebase.database.FirebaseDatabase


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dont_have_account_textview.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        login_button.setOnClickListener {
            val email = email_Login_editText.text.toString()
            val password = password_login_editText.text.toString()
            Log.d("Login", "login with  $email")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){
                    task ->  if (task.isSuccessful){
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainForum::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    Log.d("Debug", "sign in failed")
                }}.addOnFailureListener(){
                    Log.d("Debug", "Error ${it.message}")
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }

}
