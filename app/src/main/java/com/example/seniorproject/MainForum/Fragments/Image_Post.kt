package com.example.seniorproject.MainForum.Fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.seniorproject.Authentication.RegisterActivity
import com.example.seniorproject.Dagger.DaggerAppComponent

import com.example.seniorproject.R
import com.example.seniorproject.databinding.FragmentNewPostBinding
import com.example.seniorproject.utils.startRegisterActivity
import com.example.seniorproject.viewModels.NewPostFragmentViewModel
import kotlinx.android.synthetic.main.fragment_image__post.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class Image_Post : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: NewPostFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        DaggerAppComponent.create().inject(this)
        //val binding: FragmentNewPostBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_post, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(NewPostFragmentViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_image__post, container, false)
        view.add_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        val donebutton = view!!.new_image_post.setOnClickListener{
            Toast.makeText(activity?.applicationContext, "please add an image", Toast.LENGTH_SHORT).show()
        }

        return view
        //or return binding.root
    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data !=null) {
            val img: ImageView = view!!.image_preview
            selectedPhotoUri = data.data

            Glide.with(this) //1
                .load(selectedPhotoUri)
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(R.drawable.ic_account_circle_black_24dp)
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE) //4
                .into(img)

            val donebutton = view!!.new_image_post.setOnClickListener{
                val titlebox : EditText = view!!.img_post_title
                val textbox : EditText = view!!.img_post_text

                val title: String = titlebox.text.toString()
                val text: String = textbox.text.toString()
                val spinner: Spinner = view!!.spinner3
                val subject = spinner.selectedItem.toString()

                if (title.isNullOrEmpty() || text.isNullOrEmpty() || subject.isNullOrEmpty())
                {
                    Toast.makeText(activity?.applicationContext, "please add a title, post text and select a subject", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(activity?.applicationContext, "image post created", Toast.LENGTH_SHORT).show()
                    viewModel.saveNewImgPosttoUser(
                        title,
                        text,
                        subject,
                        subject,
                        selectedPhotoUri!!,
                        true
                    )
                    fragmentManager!!.beginTransaction()
                        .replace((view!!.parent as ViewGroup).id, FragmentHome())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }


    }

}
