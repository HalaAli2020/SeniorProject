package com.example.seniorproject.MainForum.Posts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.UserProfileActivity
import com.example.seniorproject.R
import com.example.seniorproject.Utils.EmailCallback
import com.example.seniorproject.databinding.ActivityEditProfileBinding
import com.example.seniorproject.viewModels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_edit_profile.*
import javax.inject.Inject

class EditProfileActivity : AppCompatActivity() {



    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //setting actionbar title
        val actionbar = supportActionBar
        actionbar!!.title = "Edit Profile"

        //initalization of the viewmodel and dagger app component
        DaggerAppComponent.create().inject(this)

         myViewModel = ViewModelProvider(this,factory).get(ProfileViewModel::class.java)

        //initialization of binding variable, binded variables are located in the corresponding XML file
        val binding: ActivityEditProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile)

        //set bio
        myViewModel.fetchBio(FirebaseAuth.getInstance().currentUser?.uid ?: "no bio",object :
            EmailCallback {
            override fun getEmail(string: String) {
                in_edit_bio.setText(string)
            }
        })

        //sets data binding variable in xml to this view model
        binding.profileEditViewModel = myViewModel
        binding.lifecycleOwner = this

        myViewModel.getclassnamesforusername()
        myViewModel.sendClassnameForUsername()

        //setting button to change the users profile image
      val img : ImageButton = findViewById(R.id.img_button)

        Glide.with(this) //1
            .load(R.mipmap.ic_edit_profile_photo_round)
            .placeholder(R.mipmap.ic_edit_profile_photo_round)
            .error(R.mipmap.ic_edit_profile_photo_round)
            .skipMemoryCache(true) //2
            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
            .apply(RequestOptions().circleCrop())//4
            .into(img)

        img.setOnClickListener {
            //opens gallery
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)}

        //button to click that saves the users profile changes
        doneButton.setOnClickListener {
            clearGlideCache()
            val newUsername : EditText = findViewById(R.id.in_profile_username)
            val newBio : EditText = findViewById(R.id.in_edit_bio)
            if (newUsername.text.toString() != myViewModel.currentUser()?.displayName && newUsername.text.toString()!="")
            {
                myViewModel.saveNewUsername(newUsername.text.toString())
            }
            if(newUsername.text.toString()==""){
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            }
            myViewModel.saveUserbio(newBio.text.toString())

            val intent = Intent(this, UserProfileActivity::class.java)
            val iD = FirebaseAuth.getInstance().currentUser?.uid
            intent.putExtra("UserID",iD).also {
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
                finish()
            }
        }
    }

    private var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data !=null)
        {
            //choosing and saving new image using Glide library
            val img : ImageButton = findViewById(R.id.img_button)

            selectedPhotoUri= data.data

            Glide.with(this) //1
                .load(selectedPhotoUri)
                .placeholder(R.drawable.ic_account_circle_blue_24dp)
                .error(R.drawable.ic_account_circle_blue_24dp)
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                .apply(RequestOptions().circleCrop())//4
                .into(img)

            //saves profile image to database
            myViewModel.uploadUserProfileImage(selectedPhotoUri ?: Uri.EMPTY)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun clearGlideCache() {
        object : Thread() {
            override fun run() {
                Glide.get(this@EditProfileActivity).clearDiskCache()
            }
        }.start()
        Glide.get(this@EditProfileActivity).clearMemory()
    }
}
