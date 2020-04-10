package com.example.seniorproject.MainForum.NewPost

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.MainForum
import com.example.seniorproject.MainForum.Fragments.FragmentList
import com.example.seniorproject.R
import com.example.seniorproject.Utils.CheckCallback
import com.example.seniorproject.viewModels.NewPostFragmentViewModel
import kotlinx.android.synthetic.main.fragment_image__post.view.*
import javax.inject.Inject


class FragmentNewImagePost : Fragment() {
    //inject and initialize viewmodel and factoru variables
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
        activity?.title = "New Image Post"
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

        //toast message for the case of no image, there is no need for logic because the post button is overridden in the activity result
         view!!.new_image_post.setOnClickListener{
            Toast.makeText(activity?.applicationContext, "please add an image", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data !=null) {
            val img: ImageView = view!!.image_preview
            selectedPhotoUri = data.data

            Glide.with(this) //1
                .load(selectedPhotoUri)
                .placeholder(R.color.white)
                .error(R.color.black)
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE) //4
                .into(img)

           view!!.new_image_post.setOnClickListener{
                   var classname = view!!.spinner3.selectedItem.toString()
                   viewModel.checkSubscriptions(classname, object : CheckCallback {
                       override fun check(chk: Boolean) {
                           if (view!!.img_post_title.text.isNotBlank() && view!!.img_post_text.text.isNotBlank() && chk == true) {
                               viewModel.saveNewImgPosttoUser(view!!.img_post_title.text.toString(), view!!.img_post_text.text.toString(), classname, selectedPhotoUri!!, true)
                               Toast.makeText(context, "Your post has been successfully posted!", Toast.LENGTH_LONG).show()
                               val intent = Intent(context, MainForum::class.java)
                               startActivity(intent)
                               return
                           }
                           else if ((view!!.img_post_title.text.isNullOrBlank() || view!!.img_post_text.text.isNullOrBlank())) {
                               Toast.makeText(context, "please enter a title and post body", Toast.LENGTH_LONG).show()
                           }
                           else if (view!!.img_post_title.text.isNotBlank() && view!!.img_post_text.text.isNotBlank() && chk == false) {
                               Toast.makeText(context, "Subscribe to $classname in order to create a post", Toast.LENGTH_SHORT).show()
                           }
                       }
                   })
               }

            }
        }

}
