package com.example.seniorproject.MainForum.Fragments


import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.MainForum.Adapters.CustomViewHolders
import com.example.seniorproject.MainForum.Adapters.PostAdapter
import com.example.seniorproject.MainForum.Adapters.PostImageViewHolders
import com.example.seniorproject.MainForum.Posts.UpdatePost
import com.example.seniorproject.MainForum.UserProfileActivity
import com.example.seniorproject.R
import com.example.seniorproject.Utils.ButtonClickListener
import com.example.seniorproject.Utils.ProfileButton
import com.example.seniorproject.Utils.SwipeHelper
import com.example.seniorproject.data.interfaces.PostListFromFlow
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.databinding.FragmentProfilePostBinding
import com.example.seniorproject.viewModels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile__post.view.*
import javax.inject.Inject


class ProfilePostFragment : Fragment() {

    private lateinit var adapter: PostAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ProfileViewModel


    //initalization of dagger app component
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerAppComponent.create().inject(this)

    }

    //initalization of the viewmodel,= binding variable
    //Binded variables are located in the corresponding XML file
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        myViewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_profile__post, container, false)
        val binding: FragmentProfilePostBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile__post, container, false)
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid ?: "null"

        //grabs uid of the profile user you are viewing. If it is null, then that means you are viewing your own profile, so we convert
        //to currentuser
        var iD = this.arguments?.getString("ID") ?: "null"
        if (iD == "null")
            iD = currentuser


        myViewModel.getUserProfilePosts(iD, object : PostListFromFlow {
            override fun onList(list: List<Post>) {
                //this is where we grab the list of posts from the callback that converted the Flow of posts to list with .toList
                //extenstion
                //setting layout so newest posts load first
                val linearLayoutManager = LinearLayoutManager(context)
                linearLayoutManager.reverseLayout = true
                linearLayoutManager.stackFromEnd = true
                adapter = PostAdapter(view.context, list, 0)
                view.profile_post_recyclerView.adapter = adapter
                view.profile_post_recyclerView.layoutManager = linearLayoutManager

                if(iD == FirebaseAuth.getInstance().uid){
                    object : SwipeHelper(context!!, view.profile_post_recyclerView, 200) {
                        override fun initButton(
                            viewHolders: RecyclerView.ViewHolder,
                            buffer: MutableList<ProfileButton>
                        ) {
                            buffer.add(
                                ProfileButton(context!!, "Delete", 45, 0, Color.parseColor
                                    ("#FF0000"), object : ButtonClickListener {
                                    override fun onClick(pos: Int) {
                                        var postkey = " "
                                        //if statement to cover image post case
                                        if (adapter.getItemViewType(pos) == 1) {
                                            postkey = adapter.removeItem(viewHolders as PostImageViewHolders)
                                        } else if (adapter.getItemViewType(pos) == 0) {

                                            postkey = adapter.removeItem(viewHolders as CustomViewHolders)
                                        }

                                        val userkey: String? =
                                            adapter.getUserKey(viewHolders)

                                        val crnkey: String? =
                                            adapter.getCrn(viewHolders)

                                        //alert dialog setup
                                        val builder = AlertDialog.Builder(
                                            view.context,
                                            R.style.AppTheme_AlertDialog
                                        )
                                        //creating message to stop user from deleting posts on accident
                                        builder.setTitle("Are you sure?")
                                        builder.setMessage("You cannot restore posts that have been deleted.")
                                        builder.setPositiveButton("DELETE"
                                        ) { _: DialogInterface?, _: Int ->
                                            myViewModel.deletePost(postkey, crnkey!!, userkey!!)
                                            val intent = Intent(view?.context, UserProfileActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                            //restarts user profile activity
                                            startActivity(intent)
                                        }
                                        builder.setNegativeButton("CANCEL"
                                        ) { _: DialogInterface?, _: Int ->
                                            builder.setCancelable(true)
                                        }

                                        val msgdialog: AlertDialog = builder.create()

                                        msgdialog.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL)

                                        msgdialog.show()

                                    }

                                })
                            )
                            //adding an edit butto on onswipe
                            buffer.add(
                                ProfileButton(context!!, "Edit", 45, 0, Color.parseColor
                                    ("#2b99fd"), object : ButtonClickListener {
                                    override fun onClick(pos: Int) {
                                        val intent = Intent(context, UpdatePost::class.java)
                                        var postkey = " "
                                        //if statement to cover image post case
                                        if (adapter.getItemViewType(pos) == 1) {
                                            postkey = adapter.removeItem(viewHolders as PostImageViewHolders)
                                        } else if (adapter.getItemViewType(pos) == 0) {

                                            postkey = adapter.removeItem(viewHolders as CustomViewHolders)
                                        }

                                        val userkey: String? =
                                            adapter.getUserKey(viewHolders)

                                        val crnkey: String? =
                                            adapter.getCrn(viewHolders)

                                        val titlekey: String? =
                                            adapter.getTitle(viewHolders)

                                        val textkey: String? =
                                            adapter.getText(viewHolders)
                                        //sending information and setting up edit post activity
                                        intent.putExtra("crn", crnkey)
                                        intent.putExtra("Classkey", postkey)
                                        intent.putExtra("UserID", userkey)
                                        intent.putExtra("title", titlekey)
                                        intent.putExtra("text", textkey)

                                        //open to update post screen
                                        context!!.startActivity(intent)
                                    }

                                })
                            )

                        }
                    }
                }
            }
        })

        binding.profViewModel = myViewModel
        binding.lifecycleOwner = this

        binding.executePendingBindings()



        return view
    }

    companion object {
        fun newInstance(ID: String): ProfilePostFragment {
            val bundle: Bundle = Bundle()
            bundle.putString("ID", ID)
            val fragment = ProfilePostFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}
