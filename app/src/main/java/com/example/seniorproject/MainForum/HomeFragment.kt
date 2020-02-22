package com.example.seniorproject.MainForum

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
//import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.Utils.PostListener
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.R
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
//import javax.inject.Inject
import javax.inject.Inject
import javax.inject.Named
import com.example.seniorproject.InjectorUtils
import com.example.seniorproject.data.repositories.PostRepository
import com.google.common.collect.Collections2.transform
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import java.time.Duration




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
     lateinit var adapter: CustomAdapter
    private lateinit var postFlow: Flow<List<Post>>
    //private lateinit var postLiveDataValue: PostLiveData

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
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        myViewModel = ViewModelProviders.of(this, factory).get(HomeFragmentViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        postFlow = myViewModel.getSavedPosts()

        


        // adapter = CustomAdapter(view.context, postLiveData.flatMapLatest{myViewModel.getSavedPosts()}.asLiveData)
        /*adapter = CustomAdapter(view.context, postLiveData.flatMapLatest{
            coroutineScope { it.map  {
                // Perform transform in parallel
                async {

                }
            }.awaitAll() }.asLiveData*/

        /*
        this is the last stop for flow. postliveData should be fetchPostLiveData function in viewModel
        thats the one with viewModelScope and the launchIn. view model will collect the flow and scope will
        be launched in fetchPostLiveData()
         */

        //adapter = CustomAdapter(view.context, finalLiveData)//.asLiveData)


        /*  GlobalScope.launch{
            val finalLiveData : List<Post> = postFlow.toList()
            adapter = CustomAdapter(view.context, finalLiveData)

        }*/

        /*CoroutineScope(Main).launch{
           val finalLiveData : List<Post> = postFlow.toList()
           adapter = CustomAdapter(view.context, finalLiveData)


       }*/

            adapter = CustomAdapter(view.context, postFlow)
            view.post_recyclerView.adapter = adapter
            view.post_recyclerView.layoutManager = LinearLayoutManager(context)
            view.post_recyclerView.adapter = adapter
            binding.homeFragmentViewModel = myViewModel
            binding.lifecycleOwner = this
            binding.postRecyclerView.adapter = adapter
            binding.executePendingBindings()


            return view


    }

    }





