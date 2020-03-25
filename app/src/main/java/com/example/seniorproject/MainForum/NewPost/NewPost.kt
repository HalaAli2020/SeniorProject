package com.example.seniorproject.MainForum.NewPost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.seniorproject.MainForum.Fragments.FragmentNewImagePost
import com.example.seniorproject.MainForum.Fragments.FragmentNewPost
import com.example.seniorproject.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewPost : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.post_bottom_navigation)

        replaceFragment(FragmentNewPost())

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_text->{
                    replaceFragment(FragmentNewPost())
                }
                R.id.navigation_image->{
                    replaceFragment(FragmentNewImagePost())
                }
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.NewPostContainer, fragment)
        fragmentTransaction.commit()
    }

}
