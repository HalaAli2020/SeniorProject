package com.example.seniorproject.MainForum.NewPost

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.MainForum
import com.example.seniorproject.R
import com.example.seniorproject.Utils.CheckCallback
import com.example.seniorproject.databinding.FragmentNewPostBinding
import com.example.seniorproject.viewModels.NewPostFragmentViewModel
import kotlinx.android.synthetic.main.fragment_new_post.view.*
import javax.inject.Inject

class FragmentNewPost : Fragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: NewPostFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = "New Text Post"
        DaggerAppComponent.create().inject(this)
        viewModel = ViewModelProviders.of(this, factory).get(NewPostFragmentViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_new_post, container, false)

        view.new_post_btn.setOnClickListener {
                var classname = view.spinner2.selectedItem.toString()
                viewModel.checkSubscriptions(classname, object : CheckCallback {
                    override fun check(chk: Boolean) {
                        if (view.new_post_text.text.isNotBlank() && view.new_post_title.text.isNotBlank() && chk == true) {
                            viewModel.savePostToDatabase(view.new_post_title.text.toString(),view.new_post_text.text.toString(),classname)
                            Toast.makeText(context, "Your post has been successfully posted!", Toast.LENGTH_LONG).show()
                            val intent = Intent(context, MainForum::class.java)
                            startActivity(intent)
                        }
                        else if ((view.new_post_text.text.isNullOrBlank() || view.new_post_title.text.isNullOrBlank())) {
                            Toast.makeText(context, "please enter a title and post body", Toast.LENGTH_LONG).show()
                        }
                        else if (view.new_post_text.text.isNotBlank() && view.new_post_title.text.isNotBlank() && chk == false) {
                            Toast.makeText(context, "Subscribe to $classname in order to create a post", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }

            val binding: FragmentNewPostBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_post, container, false)
            val adapter = ArrayAdapter.createFromResource(
                view.context,
                R.array.class_list,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            view.spinner2.adapter = adapter

            binding.newPostViewModel = viewModel
            binding.lifecycleOwner = this

        binding.executePendingBindings()
        return view
    }



}
