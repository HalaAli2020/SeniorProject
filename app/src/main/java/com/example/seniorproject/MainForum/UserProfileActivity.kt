package com.example.seniorproject.MainForum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.CustomAdapter
import com.example.seniorproject.MainForum.Fragments.ProfileCommentFragment
import com.example.seniorproject.MainForum.Fragments.ProfilePostFragment
import com.example.seniorproject.MainForum.Posts.EditProfileActivity
import com.example.seniorproject.R
import com.example.seniorproject.databinding.ActivityUserProfileBinding
import com.example.seniorproject.viewModels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_forum.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import javax.inject.Inject
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.isInvisible


private const val TAG = "profileTAG"
class UserProfileActivity : AppCompatActivity() {
    private lateinit var adapter: CustomAdapter
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val actionbar = supportActionBar
        actionbar!!.title = "Profile"
        replaceFragment(ProfileCommentFragment())
        replaceFragment(ProfilePostFragment())

        DaggerAppComponent.create().inject(this)
        val factory = InjectorUtils.provideProfileViewModelFactory()
        myViewModel = ViewModelProviders.of(this,factory).get(ProfileViewModel::class.java)

        var test : String = intent.getStringExtra("UserID") ?: "null"
        val author : String =  intent.getStringExtra("Author") ?: "null"
        var ID = test

        if (test == "null" || test == FirebaseAuth.getInstance().currentUser?.uid){
            val nav: TextView = findViewById(R.id.NavToEdit)
            Log.d(TAG,"user profile opened")
            nav.visibility = View.VISIBLE
        }
        else {
            val nav: TextView = findViewById(R.id.NavToEdit)
            nav.visibility = View.INVISIBLE
        }

        val email = myViewModel.fetchEmail(test)
        val bio = myViewModel.fetchBio(test)
        val profilepostfrag = ProfilePostFragment.newInstance(ID)
        val profilecommentfrag = ProfileCommentFragment.newInstance(ID)
        replaceFragment(profilepostfrag)

        if (author != "null")  {
            in_profile_username.text = author
            in_profile_email.text = email
            if (bio == "null"){
                in_profile_bio.text = "no bio"
            }
            else{
                in_profile_bio.text = bio
            }
        }
        else if (author == "null") {
            in_profile_username.text = myViewModel.user?.displayName
            in_profile_email.text = myViewModel.user?.email
            if (bio == "null"){
                in_profile_bio.text = myViewModel.fetchBio(FirebaseAuth.getInstance().currentUser?.uid ?: "no bio")
            }
            else{
                in_profile_bio.text = bio
            }

        }


        refresh_profile.setOnClickListener {
            refresh()
        }

       /* val binding: ActivityUserProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_user_profile)
        binding.profileViewModell = myViewModel
        binding.lifecycleOwner = this */
        actionbar.setDisplayHomeAsUpEnabled(true)

        pro_bottom_navigation.setIconVisibility(false)
        pro_bottom_navigation.enableAnimation(false)
        pro_bottom_navigation.setTextSize(20F)


        pro_bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.select_posts -> {
                    replaceFragment(profilepostfrag)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.select_comments -> {
                    replaceFragment(profilecommentfrag)
                    return@setOnNavigationItemSelectedListener true
                }

            }
            true
        }




        NavToEdit.setOnClickListener {
               navToEdit()
            }


        val image : ImageView = findViewById(R.id.in_profile_image)

        Glide.with(this) //1
            .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
            .placeholder(R.drawable.ic_account_circle_blue_24dp)
            .error(R.drawable.ic_account_circle_blue_24dp)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .apply(RequestOptions().circleCrop())//4
            .into(image)


    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fContainer, fragment)
        fragmentTransaction.commit()
    }

    private fun navToEdit() {
        Intent(this, EditProfileActivity::class.java).also {
            startActivity(it)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val intent = Intent(this, MainForum::class.java)
        startActivity(intent)
        return true
    }

    fun refresh(){
        recreate()
    }

    fun makeInvisible(view: View){

    }
}
