package com.example.seniorproject.MainForum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.InjectorUtils
import com.example.seniorproject.R
import com.example.seniorproject.databinding.ActivityUserProfileBinding
import com.example.seniorproject.databinding.SideNavHeaderBinding
import com.example.seniorproject.viewModels.ProfileViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

        actionbar.setDisplayHomeAsUpEnabled(true)

        DaggerAppComponent.create().inject(this)
        val factory = InjectorUtils.provideProfileViewModelFactory()
        myViewModel = ViewModelProviders.of(this,factory).get(ProfileViewModel::class.java)

        val binding: ActivityUserProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_user_profile)
        binding.profileViewModell = myViewModel
        binding.lifecycleOwner = this


        val imageView : ImageView = findViewById(R.id.in_profile_image)

        Glide.with(this) //1
            .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
            .placeholder(R.drawable.ic_account_circle_black_24dp)
            .error(R.drawable.ic_log_out)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .apply(RequestOptions().circleCrop())//4
            .into(imageView)

        myViewModel.getUserProfilePosts()
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        user_profile_recyclerView.layoutManager = linearLayoutManager
        user_profile_recyclerView.adapter = CustomAdapter(this, myViewModel.getUserProfilePosts())





    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val intent = Intent(this, MainForum::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        return true
    }
}
