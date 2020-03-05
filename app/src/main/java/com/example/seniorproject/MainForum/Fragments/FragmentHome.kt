package com.example.seniorproject.MainForum.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Authentication.LoginActivity
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.CustomAdapter
import com.example.seniorproject.R
import com.example.seniorproject.data.models.User
import com.example.seniorproject.databinding.FragmentHomeBinding
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.view.*
import javax.inject.Inject


class FragmentHome : Fragment() {


    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: HomeFragmentViewModel


    companion object {
        var currentUser: User? = null
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        /*DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProviders.of(this,factory).get(HomeFragmentViewModel::class.java)
        val binding: FragmentHomeBinding = inflate(inflater, R.layout.fragment_home, container, false)
        val view = inflater.inflate(R.layout.fragment_home, container, false)*/

        activity?.title = "Home"
        LoginVerification()

        val factory = InjectorUtils.providePostViewModelFactory()
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        myViewModel = ViewModelProviders.of(this, factory).get(HomeFragmentViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        //postLiveData = myViewModel.getSavedPosts()


        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        view.post_recyclerView.layoutManager = linearLayoutManager

        if (FirebaseAuth.getInstance().uid != null)
            view.post_recyclerView.adapter = CustomAdapter(view.context, myViewModel.getSubscribedPosts(), 0)


        view.refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(view.context, R.color.blue_theme))
        view.refreshView.setColorSchemeColors(ContextCompat.getColor(view.context, R.color.white))


        view.refreshView.setOnRefreshListener {
            view.refreshView.isRefreshing = false
            view.post_recyclerView.adapter = CustomAdapter(view.context, myViewModel.getSubscribedPosts(), 0)
        }

        binding.homeFragmentViewModel = myViewModel
        binding.lifecycleOwner = this

        binding.executePendingBindings()



        return view

    }


    private fun LoginVerification() {
        if (FirebaseAuth.getInstance().uid == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    /* private fun listenForPosts(){
         val reference = FirebaseDatabase.getInstance().getReference("/posts")

         reference.addChildEventListener(object: ChildEventListener{
             override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                 val newPost = p0.getValue(Post::class.java)

                 if(newPost!=null) {
                     Log.d("ForumACT", newPost?.text)
                     //adapter.add(Post(newPost.title, newPost.text, 0, ""))
                 }
             }

             override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }

    private fun performNewPost(){
        val title = new_post_title.text.toString()
        val text = new_post_text.text.toString()
        val reference = FirebaseDatabase.getInstance().getReference("/posts").push()

        if(title.isNotEmpty() && text.isNotEmpty()) {
            val post = Post(title, text, 0, "")

            reference.setValue(post).addOnSuccessListener {
                Log.d("PostForum", "Saved our post sucessfully to database: ${reference.key}")
                new_post_text.setText("")
                new_post_title.setText("")
            }
        }
    }

*/


}




