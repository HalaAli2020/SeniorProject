package com.example.seniorproject.MainForum.NewPost

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Fragments.FragmentList
import com.example.seniorproject.MainForum.MainForum

import com.example.seniorproject.R
import com.example.seniorproject.viewModels.NewPostFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_image__post.view.*
import javax.inject.Inject


class FragmentNewImagePost : Fragment() {

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

         view!!.new_image_post.setOnClickListener{
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
                .placeholder(R.color.white)
                .error(R.color.black)
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE) //4
                .into(img)

           view!!.new_image_post.setOnClickListener{
                val titlebox : EditText = view!!.img_post_title
                val textbox : EditText = view!!.img_post_text

                val title: String = titlebox.text.toString()
                val text: String = textbox.text.toString()
                val spinner: Spinner = view!!.spinner3
                val subject = spinner.selectedItem.toString()

                if (title.isNullOrEmpty() || text.isNullOrEmpty() || subject.isNullOrEmpty())
                {
                    Toast.makeText(activity?.applicationContext, "Please add a title, post text and select a subject.", Toast.LENGTH_SHORT).show()
                }
                else {
                    val userID= FirebaseAuth.getInstance().uid
                    val subpath = FirebaseDatabase.getInstance().getReference("/users/$userID")
                    subpath.child("Subscriptions").orderByValue()
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.exists()) {
                                    for (sub in p0.children) {
                                        // val refc= comment.getValue(Comment::class.java)
                                        if (sub.getValue() == subject) {
                                            viewModel.saveNewImgPosttoUser(
                                                title,
                                                text,
                                                subject,
                                                selectedPhotoUri!!,
                                                true
                                            )
                                            Toast.makeText(activity?.applicationContext, "Image post created", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(context, MainForum::class.java)
                                            startActivity(intent)
                                        }
                                        else{
                                            Toast.makeText(activity?.applicationContext, "Subscribe to $subject in order to create a post", Toast.LENGTH_SHORT).show()
                                            fragmentManager!!.beginTransaction()
                                                .replace((view!!.parent as ViewGroup).id,
                                                    FragmentList()
                                                )
                                                .addToBackStack(null)
                                                .commit()
                                        }

                                    }
                                }
                            }

                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }
                        })


                }
            }
        }


    }

}
