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
//figure out the new name
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


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), PostListener {


    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: HomeFragmentViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var adapter: CustomAdapter
    private lateinit var postLiveData: PostLiveData

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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DaggerAppComponent.create().inject(this)
        viewModel = ViewModelProviders.of(this,factory).get(HomeFragmentViewModel::class.java)
        val binding: FragmentHomeBinding =
            inflate(inflater, R.layout.fragment_home, container, false)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        postLiveData = viewModel.getSavedPosts()


        adapter = CustomAdapter(postLiveData)
        view.post_recyclerView.adapter = adapter
        view.post_recyclerView.layoutManager = LinearLayoutManager(context)
        view.post_recyclerView.adapter = adapter
        binding.homeFragmentViewModel = viewModel
        binding.lifecycleOwner = this

        binding.executePendingBindings()
        DaggerAppComponent.create().inject(this)
        myViewModel = ViewModelProviders.of(this, factory).get(HomeFragmentViewModel::class.java)
        myViewModel.getSavedPosts()
        //myViewModel.postListener = this

        while (PostLiveData.get().value != null) {

            adapter = CustomAdapter(postLiveData)
            view.post_recyclerView.adapter = adapter
            view.post_recyclerView.layoutManager = LinearLayoutManager(context)
            view.post_recyclerView.adapter = adapter
            binding.homeFragmentViewModel = viewModel
            binding.lifecycleOwner = this

            binding.executePendingBindings()
        }


        return view

    }







}




