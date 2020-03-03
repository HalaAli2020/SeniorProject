package com.example.seniorproject.MainForum

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
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
import com.example.seniorproject.R
import com.example.seniorproject.databinding.ActivityUserProfileBinding
import com.example.seniorproject.viewModels.ProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_profile.*
import javax.inject.Inject

private const val TAG = "profileTAG"
class UserProfileActivity : AppCompatActivity() {
    private lateinit var adapter: CustomAdapter
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ProfileViewModel

   /* private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.select_posts -> {
                    replaceFragment(ProfilePostFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.select_comments -> {
                    replaceFragment(ProfileCommentFragment())
                    return@OnNavigationItemSelectedListener true
                }

            }
            true
        }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //val nav = findViewById<BottomNavigationView>(R.id.pro_bottom_navigation)
       // pro_bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val actionbar = supportActionBar
        actionbar!!.title = "Profile"
        replaceFragment(ProfilePostFragment())
        DaggerAppComponent.create().inject(this)
        val factory = InjectorUtils.provideProfileViewModelFactory()
        myViewModel = ViewModelProviders.of(this,factory).get(ProfileViewModel::class.java)

        val binding: ActivityUserProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_user_profile)
        binding.profileViewModell = myViewModel
        binding.lifecycleOwner = this
        actionbar.setDisplayHomeAsUpEnabled(true)

        pro_bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.select_posts -> {
                    replaceFragment(ProfilePostFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.select_comments -> {
                    replaceFragment(ProfileCommentFragment())
                    return@setOnNavigationItemSelectedListener true
                }

            }
            true
        }


        //val image : ImageButton = findViewById(R.id.in_profile_image)

      /*  Glide.with(this) //1
            .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
            .placeholder(R.drawable.ic_account_circle_black_24dp)
            .error(R.drawable.ic_log_out)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .apply(RequestOptions().circleCrop())//4
            .into(image) */

        //image.setOnClickListener {
        //    val intent = Intent(Intent.ACTION_PICK)
        //    intent.type = "image/*"
        // startActivityForResult(intent, 0)}

       // myViewModel.getUserProfilePosts()
        //val linearLayoutManager = LinearLayoutManager(this)
        //linearLayoutManager.reverseLayout = true
        //linearLayoutManager.stackFromEnd = true
        //user_profile_recyclerView.layoutManager = linearLayoutManager
        //user_profile_recyclerView.adapter = CustomAdapter(this, myViewModel.getUserProfilePosts(),1)

        /*myViewModel.getUserProfilePosts()
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        user_profile_recyclerView.layoutManager = linearLayoutManager
        user_profile_recyclerView.adapter =
            CustomAdapter(
                this,
                myViewModel.getUserProfilePosts(),
                1
            )*/


    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data !=null)
        {

           /* selectedPhotoUri= data.data

            Glide.with(this) //1
                .load(selectedPhotoUri)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_log_out)
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                .apply(RequestOptions().circleCrop())//4
                .into(in_profile_image)

            myViewModel.uploadUserProfileImage(selectedPhotoUri ?: Uri.EMPTY)*/

        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fContainer, fragment)
        fragmentTransaction.commit()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val intent = Intent(this, MainForum::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        return true
    }
}
