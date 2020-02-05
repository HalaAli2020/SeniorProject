package com.example.seniorproject.MainForum

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.seniorproject.InjectorUtils
import com.example.seniorproject.PostListener
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import com.example.seniorproject.data.models.User
///import com.example.seniorproject.databinding.FragmentHomeBinding
import com.example.seniorproject.viewModels.AuthenticationViewModel
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.post_rv.view.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    // test comment
    companion object {
        var currentUser: User? = null

    }

    private lateinit var viewModel: HomeFragmentViewModel
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.post_recyclerView.adapter = adapter
        fetchCurrentUser()
        listenForPosts()

        view.new_post_btn.setOnClickListener {
            performNewPost()
            listenForPosts()
            view.post_recyclerView.scrollToPosition(0)
        }


        return view

        /*val factory = InjectorUtils.providePostViewModelFactory()
        val binding: FragmentHomeBinding = inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(HomeFragmentViewModel::class.java)
        viewModel.getSavedPosts()

        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this

        //viewModel.postListener = this

        return binding.root*/
    }




    private fun listenForPosts(){
        val reference = FirebaseDatabase.getInstance().getReference("/posts")

        reference.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val newPost = p0.getValue(Post::class.java)

                if(newPost!=null) {
                    Log.d("ForumACT", newPost?.text)
                    adapter.add(Post(newPost.title, newPost.text, 0, ""))
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


    private fun fetchCurrentUser(){
        var uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d("LatestMessages", "Current user ${currentUser?.username}")
                val usernameForum = currentUser?.username
                username_forum.text = "Welcome " + usernameForum
            }
        })
    }





}




