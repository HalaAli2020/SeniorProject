package com.example.seniorproject.MainForum.Posts

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.UserProfileActivity
import com.example.seniorproject.R
import com.example.seniorproject.databinding.UpdatePostBinding
import com.example.seniorproject.viewModels.NewPostFragmentViewModel
import javax.inject.Inject

class UpdatePost : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: NewPostFragmentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_post)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Update Post"


        //initialize inject of dagger app component and initializes view model with generic factory
        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProvider(this, factory).get(NewPostFragmentViewModel::class.java)
        val binding: UpdatePostBinding = DataBindingUtil.setContentView(this,R.layout.update_post)

        //gets post information
        val text: String = intent.getStringExtra("text") ?: "no text"
        val title: String = intent.getStringExtra("title") ?: "no title"
        val postkey: String = intent.getStringExtra("Classkey") ?: "class key"
        val userid: String = intent.getStringExtra("UserID") ?: "no User id"
        val crn: String = intent.getStringExtra("crn") ?: "no crn"

        myViewModel.ctext = text
        myViewModel.ctitle=title
        myViewModel.userID= userid
        myViewModel.crn = crn
        myViewModel.postKey = postkey

        //bool is a controlled variable that controls the toast messages
        myViewModel.bool.observe(this, Observer<Boolean> {
            if (it == true ){
                Toast.makeText(this , "Your post has been successfully changed!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, UserProfileActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            else if (it == false)
            {
                Toast.makeText(this , "you cannot edit your post with a blank text or title", Toast.LENGTH_LONG).show()
            }

        })


        //sets data binding variable in xml to this view model
        binding.newPostModel = myViewModel
        binding.lifecycleOwner = this

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}