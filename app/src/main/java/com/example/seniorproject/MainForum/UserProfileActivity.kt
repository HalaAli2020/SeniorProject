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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


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


        NavToEdit.setOnClickListener {
               navToEdit()
            }


        val image : ImageView = findViewById(R.id.in_profile_image)

        Glide.with(this) //1
            .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
            .placeholder(R.mipmap.ic_holder_round)
            .error(R.mipmap.ic_holder_round)
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
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or (Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(it)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val intent = Intent(this, MainForum::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        return true
    }
}
