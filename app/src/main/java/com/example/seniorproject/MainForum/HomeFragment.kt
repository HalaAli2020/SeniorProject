package com.example.seniorproject.MainForum

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
//import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.R
import com.example.seniorproject.data.models.PostLiveData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import com.example.seniorproject.data.models.User
import com.example.seniorproject.databinding.FragmentHomeBinding
import com.example.seniorproject.databinding.NewPostFragmentBinding
import com.example.seniorproject.viewModels.AuthenticationViewModel
import com.example.seniorproject.viewModels.HomeFragmentViewModel
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.post_rv.view.*
import kotlinx.coroutines.awaitAll
//import javax.inject.Inject
import javax.inject.Inject
import javax.inject.Named
import com.example.seniorproject.InjectorUtils

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), PostListener {


    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: HomeFragmentViewModel
    override fun onStarted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFailure(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCancelled(p0: DatabaseError) {

    }

    override fun onDataChange(p0: DataSnapshot) {

    }

    // test comment
    companion object {
        var currentUser: User? = null

    }

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CustomAdapter
    private lateinit var postLiveData: PostLiveData

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

        val factory = InjectorUtils.providePostViewModelFactory()
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        myViewModel = ViewModelProviders.of(this, factory).get(HomeFragmentViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        postLiveData = myViewModel.getSavedPosts()

        adapter = CustomAdapter(view.context, postLiveData)
        view.post_recyclerView.adapter = adapter
        view.post_recyclerView.layoutManager = LinearLayoutManager(context)
        view.post_recyclerView.adapter = adapter
        binding.homeFragmentViewModel = myViewModel
        binding.lifecycleOwner = this


        /*DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProviders.of(this, factory).get(HomeFragmentViewModel::class.java)*/

        myViewModel.getSavedPosts()
        //myViewModel.postListener = this


        binding.executePendingBindings()

        while (PostLiveData.get().value != null) {

            adapter = CustomAdapter(view.context, postLiveData)
            view.post_recyclerView.adapter = adapter
            view.post_recyclerView.layoutManager = LinearLayoutManager(context)
            view.post_recyclerView.adapter = adapter
            binding.homeFragmentViewModel = myViewModel
            binding.lifecycleOwner = this

            binding.executePendingBindings()
        }

        return view

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




