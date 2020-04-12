package com.example.seniorproject.MainForum.Fragments

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.example.seniorproject.MainForum.Adapters.ProfileCommAdapter
import com.example.seniorproject.MainForum.Posts.UpdateComment
import com.example.seniorproject.R
import com.example.seniorproject.Utils.ButtonClickListener
import com.example.seniorproject.Utils.ProfileButton
import com.example.seniorproject.Utils.SwipeHelper
import com.example.seniorproject.data.interfaces.CommentListFromFlow
import com.example.seniorproject.data.models.Comment
import com.example.seniorproject.databinding.FragmentProfileCommentBinding
import com.example.seniorproject.viewModels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile_comment.view.*
import javax.inject.Inject

class ProfileCommentFragment : Fragment() {

    private lateinit var adaptercomments: ProfileCommAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ProfileViewModel

    //initalization of the viewmodel and dagger app component
    //initialization of binding variable, binded variables are located in the corresponding XML file
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerAppComponent.create().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentProfileCommentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_comment, container, false)
        myViewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_profile_comment, container, false)

        val currentuser = FirebaseAuth.getInstance().currentUser?.uid ?: "null"
        var iD = this.arguments?.getString("ID") ?: "null"
        //making sure id never equals null to avoid errors
        if (iD == "null")
            iD = currentuser


        myViewModel.getUserProfileComments(iD, object: CommentListFromFlow {
            override fun onList(list: List<Comment>) {

                //setting layout so newest comments load first
                val linearLayoutManager = LinearLayoutManager(context)
                linearLayoutManager.reverseLayout = true
                linearLayoutManager.stackFromEnd = true
                adaptercomments = ProfileCommAdapter(view.context, list)
                view.profile_comment_recyclerView.adapter = adaptercomments
                view.profile_comment_recyclerView.layoutManager = linearLayoutManager

                //settting refreshview UI
                view.refreshView.setProgressBackgroundColorSchemeColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.blue_theme
                    )
                )
                view.refreshView.setColorSchemeColors(
                    ContextCompat.getColor(
                        view.context,
                        R.color.white
                    )
                )

                view.refreshView.setOnRefreshListener {
                    view.profile_comment_recyclerView.adapter =
                        ProfileCommAdapter(view.context, list)
                    view.refreshView.isRefreshing = false
                }

                //this if statement makes sure that if you travel to another user's profile, you can't edit and delete their posts/comments
                //you can only delete and edit your own posts/comments
                if(iD == FirebaseAuth.getInstance().uid){
                object : SwipeHelper(context!!, view.profile_comment_recyclerView, 200) {
                    override fun initButton(
                        viewHolders: RecyclerView.ViewHolder,
                        buffer: MutableList<ProfileButton>
                    ) {
                        buffer.add(
                            ProfileButton(context!!, "Delete", 30, 0, Color.parseColor
                                ("#FF0000"), object : ButtonClickListener {
                                override fun onClick(pos: Int) {
                                    val postkeyUP: String? = adaptercomments.pkeyUserProfile(viewHolders as CustomViewHolders)

                                    val userkey: String? = adaptercomments.getUserKey(viewHolders)

                                    val crnkey: String? = adaptercomments.getCrn(viewHolders)

                                    val commentkey: String? = adaptercomments.getCommentKey(viewHolders)

                                    val classkey: String? = adaptercomments.getClassKey(viewHolders)

                                    val classprofilekey: String? = adaptercomments.getClassProfileKey(viewHolders)

                                    val builder = AlertDialog.Builder(
                                        view.context,
                                        R.style.AppTheme_AlertDialog
                                    )

                                    //creating dialog box and message to stop user from deleting post on accident
                                    builder.setTitle("Are you sure?")
                                    builder.setMessage("You cannot restore comments that have been deleted.")
                                    builder.setPositiveButton("DELETE"
                                    ) { _: DialogInterface?, _: Int ->
                                        myViewModel.deleteCommentFromCommPosts(
                                            postkeyUP!!,
                                            crnkey!!,
                                            classkey!!
                                        )
                                        myViewModel.deleteCommentFromUserProfile(
                                            commentkey!!,
                                            crnkey,
                                            classprofilekey!!,
                                            userkey!!
                                        )
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

                        buffer.add(
                            ProfileButton(context!!, "Edit", 30, 0, Color.parseColor
                                ("#D3D3D3"), object : ButtonClickListener {
                                override fun onClick(pos: Int) {
                                    Log.d("soupprof", "can you see me")
                                    val userkey: String? = adaptercomments.getUserKey(viewHolders as CustomViewHolders)

                                    val classprofilekey: String? = adaptercomments.getClassProfileKey(viewHolders)

                                    val textkey: String? = adaptercomments.getText(viewHolders)

                                    val crnkey: String? = adaptercomments.getCrn(viewHolders)

                                    val postkeyUP: String? = adaptercomments.pkeyUserProfile(viewHolders)

                                    //sending information and starting edit post activity
                                    val intent = Intent(context, UpdateComment::class.java)
                                    intent.putExtra("PosterID", userkey)
                                    intent.putExtra("ProfileComKey", classprofilekey)
                                    intent.putExtra("text", textkey)
                                    intent.putExtra("crn", crnkey)
                                    intent.putExtra("Postkey", postkeyUP)

                                    context!!.startActivity(intent)
                                }

                            })
                        )

                    }

                }
                }
            }
        })

        binding.profileViewModelCom = myViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.executePendingBindings()

        return view
    }

    companion object {
        fun newInstance(ID: String): ProfileCommentFragment {
            val bundle : Bundle = Bundle()
            bundle.putString("ID",ID)
            val fragment = ProfileCommentFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}
