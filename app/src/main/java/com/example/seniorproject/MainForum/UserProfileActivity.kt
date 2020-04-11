package com.example.seniorproject.MainForum

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Fragments.ProfileCommentFragment
import com.example.seniorproject.MainForum.Fragments.ProfilePostFragment
import com.example.seniorproject.MainForum.Posts.EditProfileActivity
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.viewModels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_profile.*
import javax.inject.Inject


private const val TAG = "profileTAG"

class UserProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.seniorproject.R.layout.activity_user_profile)


        //setting the actionbar title
        val actionbar = supportActionBar
        actionbar!!.title = "Profile"

        //creating the dagger application component
        DaggerAppComponent.create().inject(this)
        //initializing viewmodel factory
        val factory = InjectorUtils.provideProfileViewModelFactory()
        //setting the profileViewmodel as the viewmodel for this activity
        myViewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)


        //getting the Userid and author from the post the user selected to get to this activity
        val test: String = intent.getStringExtra("UserID") ?: "null"
        val author: String = intent.getStringExtra("Author") ?: "null"
        val iD = test

        //if test is null we can assume that the user's own profile is being opened
        if (test == "null" || test == FirebaseAuth.getInstance().currentUser?.uid) {
            val nav: TextView = findViewById(com.example.seniorproject.R.id.NavToEdit)
            Log.d(TAG, "user profile opened")
            nav.visibility = View.VISIBLE
        } else {
            val nav: TextView = findViewById(com.example.seniorproject.R.id.NavToEdit)
            nav.visibility = View.INVISIBLE
        }

        //initializing fragments as per id variable
        val profilepostfrag = ProfilePostFragment.newInstance(iD)
        val profilecommentfrag = ProfileCommentFragment.newInstance(iD)
        replaceFragment(profilepostfrag)

        // if the author is null then the email must be taken from the database
        if (author != "null") {
            myViewModel.fetchEmail(test, object : EmailCallback {
                override fun getEmail(string: String) {
                    in_profile_username.text = author
                    in_profile_email.text = string
                }
            })
            //same for the bio
            myViewModel.fetchBio(test, object : EmailCallback {
                override fun getEmail(string: String) {
                    in_profile_bio.text = string
                }
            })

        } else {
            //fetch the current users bio
            myViewModel.fetchBio(FirebaseAuth.getInstance().currentUser?.uid ?: "no bio",
                object : EmailCallback {
                    override fun getEmail(string: String) {
                        in_profile_bio.text = string
                    }
                })
            myViewModel.fetchUsername(FirebaseAuth.getInstance().currentUser?.uid ?: "no username",
                object : EmailCallback {
                    override fun getEmail(string: String) {
                        in_profile_username.text = string
                    }
                })
            in_profile_email.text = myViewModel.user?.email
        }

        //UI settings for the actionbar and navigation
        actionbar.setDisplayHomeAsUpEnabled(true)
        pro_bottom_navigation.setIconVisibility(false)
        pro_bottom_navigation.enableAnimation(false)
        pro_bottom_navigation.setTextSize(20F)

//navigation menu for controlling the visible fragment
        pro_bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                com.example.seniorproject.R.id.select_posts -> {
                    replaceFragment(profilepostfrag)
                    return@setOnNavigationItemSelectedListener true
                }
                com.example.seniorproject.R.id.select_comments -> {
                    replaceFragment(profilecommentfrag)
                    return@setOnNavigationItemSelectedListener true
                }

            }
            true
        }


//setting the edit button to navigated to the EditProfileActivity
        NavToEdit.setOnClickListener {
            navToEdit()
        }


        val image: ImageView = findViewById(com.example.seniorproject.R.id.in_profile_image)
        if (author == "null") {
            val id = FirebaseAuth.getInstance().currentUser?.uid ?: test
            myViewModel.readPhotoValue(id, object : EmailCallback {
                override fun getEmail(string: String) {
                    Log.d("Soup", "file name is $string")
                    Glide.with(this@UserProfileActivity) //1
                        .load(Uri.parse(string))
                        .placeholder(com.example.seniorproject.R.drawable.ic_account_circle_blue_24dp)
                        .error(com.example.seniorproject.R.drawable.ic_account_circle_blue_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop().fitCenter()
                        .into(image)

                }
            })
        } else {
            //getting the profile image for the another user
            Log.d("Soup", test)
            myViewModel.readPhotoValue(test, object : EmailCallback {
                override fun getEmail(string: String) {
                    Log.d("Soup", "file name is $string")
                    Glide.with(this@UserProfileActivity) //1
                        .load(Uri.parse(string))
                        .placeholder(com.example.seniorproject.R.drawable.ic_account_circle_blue_24dp)
                        .error(com.example.seniorproject.R.drawable.ic_account_circle_blue_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .circleCrop().fitCenter()
                        .into(image)

                }
            })
        }

    }

    //boilerplate code to replace a fragment
    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(com.example.seniorproject.R.id.fContainer, fragment)
        fragmentTransaction.commit()
    }

    //boilerplate code for navigation
    private fun navToEdit() {
        Intent(this, EditProfileActivity::class.java).also {
            startActivity(it)
        }
    }

    //setting up the back button to navigate to the previous screen
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val intent = Intent(this, MainForum::class.java)
        startActivity(intent)
        return true
    }

}
