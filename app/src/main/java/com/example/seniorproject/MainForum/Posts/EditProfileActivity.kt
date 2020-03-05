package com.example.seniorproject.MainForum.Posts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.CustomAdapter
import com.example.seniorproject.MainForum.UserProfileActivity
import com.example.seniorproject.R
import com.example.seniorproject.databinding.ActivityEditProfileBinding
import com.example.seniorproject.databinding.ActivityUserProfileBinding
import com.example.seniorproject.viewModels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import javax.inject.Inject

class EditProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val actionbar = supportActionBar
        actionbar!!.title = "Edit Profile"

        DaggerAppComponent.create().inject(this)
        val factory = InjectorUtils.provideProfileViewModelFactory()
        myViewModel = ViewModelProviders.of(this,factory).get(ProfileViewModel::class.java)

        val binding: ActivityEditProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile)
        binding.profileEditViewModel = myViewModel
        binding.lifecycleOwner = this


      val img : ImageButton = findViewById<ImageButton>(R.id.img_button)

        Glide.with(this) //1
            .load(R.mipmap.ic_edit_profile_photo_round)
            .placeholder(R.mipmap.ic_edit_profile_photo_round)
            .error(R.mipmap.ic_edit_profile_photo_round)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .apply(RequestOptions().circleCrop())//4
            .into(img)

        img.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)}

        doneButton.setOnClickListener {
            Intent(this, UserProfileActivity::class.java).also {
                startActivity(it)
            }



        }
    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data !=null)
        {
            val img : ImageButton = findViewById<ImageButton>(R.id.img_button)

            selectedPhotoUri= data.data

            Glide.with(this) //1
                .load(selectedPhotoUri)
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(R.drawable.ic_account_circle_black_24dp)
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                .apply(RequestOptions().circleCrop())//4
                .into(img)

            myViewModel.uploadUserProfileImage(selectedPhotoUri ?: Uri.EMPTY)

        }
    }
}
