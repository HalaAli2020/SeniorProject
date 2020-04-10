package com.example.seniorproject.MainForum.NewPost

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.seniorproject.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewPost : AppCompatActivity() {
//this activities purpose is to hold both the text post and image post fragments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
//initialization of post type navigation bar
        val bottomNavigation: BottomNavigationView = findViewById(R.id.post_bottom_navigation)
//text post fragment will show up first
        replaceFragment(FragmentNewPost())

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_text->{
                    replaceFragment(FragmentNewPost())
                    //navigate to textpost
                }
                R.id.navigation_image->{
                    replaceFragment(FragmentNewImagePost())
                    //navigate to imagepost
                }
            }
            true
        }

    }

    //function to separate replace fragment boilerplate code
    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.NewPostContainer, fragment)
        fragmentTransaction.commit()
    }

}
