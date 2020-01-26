package com.example.seniorproject



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.seniorproject.model.Parentpost
import com.example.seniorproject.model.User


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initializeForum()

    }

    private fun initializeForum(){

        val post1 = Parentpost()

        Log.d("TESTING123", post1.gettxt())

    }
}

