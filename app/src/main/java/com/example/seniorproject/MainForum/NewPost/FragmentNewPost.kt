package com.example.seniorproject.MainForum.NewPost

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.MainForum
import com.example.seniorproject.R
import com.example.seniorproject.Utils.CheckCallback
import com.example.seniorproject.viewModels.NewPostFragmentViewModel
import kotlinx.android.synthetic.main.fragment_new_post.view.*
import javax.inject.Inject

class FragmentNewPost : Fragment() {

    //inject and initialize viewmodel factoru and viewmodel
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: NewPostFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //set titlebar text
        activity?.title = "New Text Post"
        //inject dagger app component and initialize viewmodel
        DaggerAppComponent.create().inject(this)
        viewModel = ViewModelProvider(this, factory).get(NewPostFragmentViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_new_post, container, false)

        //text post is created on button clock text and title fields have text are filled and the user is subscribed to the forum
        view.new_post_btn.setOnClickListener {
                val classname = view.spinner2.selectedItem.toString()
                viewModel.checkSubscriptions(classname, object : CheckCallback {
                    override fun check(chk: Boolean) {
                        /*check varible checks is user is subscribed, the logic for this can be found in the viewmodel
                        and the database query can be found in the corresponding checksubscription firebase function
                         */
                        if (view.new_post_text.text.isNotBlank() && view.new_post_title.text.isNotBlank() && chk) {
                            viewModel.savePostToDatabase(view.new_post_text.text.toString(),view.new_post_title.text.toString(),classname)
                            Toast.makeText(context, "Your post has been successfully posted!", Toast.LENGTH_LONG).show()
                            val intent = Intent(context, MainForum::class.java)
                            startActivity(intent)
                        }
                        else if ((view.new_post_text.text.isNullOrBlank() || view.new_post_title.text.isNullOrBlank())) {
                            Toast.makeText(context, "please enter a title and post body", Toast.LENGTH_LONG).show()
                        }
                        else if (view.new_post_text.text.isNotBlank() && view.new_post_title.text.isNotBlank() && !chk) {
                            Toast.makeText(context, "Subscribe to $classname in order to create a post", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }

//initialization of the class spinner adapter
            val adapter = ArrayAdapter.createFromResource(
                view.context,
                R.array.class_list,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            view.spinner2.adapter = adapter

        return view
    }



}
