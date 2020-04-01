package com.example.seniorproject.MainForum.Fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.DaggerAppComponent
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.CustomAdapter
import com.example.seniorproject.MainForum.Adapters.CustomViewHolders
import com.example.seniorproject.MainForum.Adapters.ProfileCommentsAdapter
import com.example.seniorproject.MainForum.Posts.CommunityPosts
import com.example.seniorproject.MainForum.Posts.UpdateComment
import com.example.seniorproject.R
import com.example.seniorproject.Utils.ButtonClickListener
import com.example.seniorproject.Utils.ProfileButton
import com.example.seniorproject.Utils.SwipeHelper
import com.example.seniorproject.Utils.checkCallback
import com.example.seniorproject.data.models.CommentLive
import com.example.seniorproject.databinding.FragmentProfileCommentBinding
import com.example.seniorproject.viewModels.CommunityPostViewModel
import com.example.seniorproject.viewModels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_profile__post.view.*
import kotlinx.android.synthetic.main.fragment_profile_comment.*
import kotlinx.android.synthetic.main.fragment_profile_comment.view.*
import kotlinx.android.synthetic.main.fragment_profile_comment.view.refreshView
import kotlinx.android.synthetic.main.rv_post_comment.*
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


class ProfileCommentFragment : Fragment() {

    private lateinit var adaptercomments: ProfileCommentsAdapter
    private lateinit var adapterposts: CustomAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    lateinit var deleteIcon: Drawable

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ProfileViewModel
    lateinit var viewModel: ProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var currentuser = FirebaseAuth.getInstance().currentUser?.uid ?: "null"
        var ID = this.getArguments()?.getString("ID") ?: "null"
        if (ID == "null") {
            ID = currentuser
        }

        //call the lister up here
        DaggerAppComponent.create().inject(this)
        val factory = InjectorUtils.provideProfileViewModelFactory()
        val binding: FragmentProfileCommentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_comment, container, false)
        myViewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_profile_comment, container, false)

        if (myViewModel.getUserProfilePosts(ID).value == null){
            ID = "0"
        }


        myViewModel.comments.observe(this, androidx.lifecycle.Observer {
            swap(ID)
        } )


       view.profile_comment_recyclerView.adapter = ProfileCommentsAdapter(view.context, myViewModel.getUserProfileComments(ID))

       adaptercomments = ProfileCommentsAdapter(view.context, myViewModel.getUserProfileComments(ID))


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
                ProfileCommentsAdapter(view.context, myViewModel.getUserProfileComments(ID))
            view.refreshView.isRefreshing = false
        }


        binding.profileViewModelCom = myViewModel
        binding.lifecycleOwner = this


        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        view.profile_comment_recyclerView.layoutManager = linearLayoutManager
        view.profile_comment_recyclerView.adapter = ProfileCommentsAdapter(view.context, myViewModel.getUserProfileComments(ID))

        deleteIcon = ContextCompat.getDrawable(activity!!.applicationContext, R.drawable.ic_delete_24px)!!
        binding.executePendingBindings()

         myViewModel.noCommentsChecker(FirebaseAuth.getInstance().currentUser?.uid ?: "null", object : checkCallback {
             override fun check(chk: Boolean) {
                 if (ID != FirebaseAuth.getInstance().currentUser?.uid || chk == true){
                     Log.d("wrong","one")
                 }
                 else if (chk == false) {
                     object : SwipeHelper(context!!, view.profile_comment_recyclerView, 200) {
                         override fun initButton(
                             viewHolders: RecyclerView.ViewHolder,
                             buffer: MutableList<ProfileButton>
                         ) {
                             buffer.add(
                                 ProfileButton(context!!, "Delete", 30, 0, Color.parseColor
                                     ("#FF0000"), object : ButtonClickListener {
                                     override fun onClick(pos: Int) {
                                         val postkeyUP: String? =
                                             adaptercomments.pkeyUserProfile( //uninitialized property exeption
                                                 viewHolders as CustomViewHolders
                                             )
                                         val userkey: String? =
                                             adaptercomments.getUserKey(
                                                 viewHolders
                                             )

                                         val crnkey: String? =
                                             adaptercomments.getCrn(
                                                 viewHolders
                                             )

                                         val commentkey: String? =
                                             adaptercomments.getCommentKey(
                                                 viewHolders
                                             )

                                         val classkey: String? =
                                             adaptercomments.getClassKey(
                                                 viewHolders
                                             )

                                         val classprofilekey: String? =
                                             adaptercomments.getClassProfileKey(
                                                 viewHolders
                                             )

                                         var builder = AlertDialog.Builder(
                                             view.context,
                                             R.style.AppTheme_AlertDialog
                                         )

                                         builder.setTitle("Are you sure?")
                                         builder.setMessage("You cannot restore comments that have been deleted.")
                                         builder.setPositiveButton("DELETE",
                                             { dialogInterface: DialogInterface?, i: Int ->
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
                                             })
                                         builder.setNegativeButton("CANCEL",
                                             { dialogInterface: DialogInterface?, i: Int ->
                                                 builder.setCancelable(true)
                                             })

                                         val msgdialog: AlertDialog = builder.create()
                                         msgdialog.getWindow()!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL)
                                         msgdialog.show()

                                     }

                                 })
                             )

                             buffer.add(
                                 ProfileButton(context!!, "Edit", 30, 0, Color.parseColor
                                     ("#D3D3D3"), object : ButtonClickListener {
                                     override fun onClick(pos: Int) {
                                         val userkey: String? =
                                             adaptercomments.getUserKey(
                                                 viewHolders as CustomViewHolders
                                             )

                                         val classprofilekey: String? =
                                             adaptercomments.getClassProfileKey(
                                                 viewHolders
                                             )

                                         val textkey: String? = adaptercomments.getText(
                                             viewHolders
                                         )

                                         val crnkey: String? =
                                             adaptercomments.getCrn(
                                                 viewHolders
                                             )

                                         val postkeyUP: String? =
                                             adaptercomments.pkeyUserProfile(
                                                 viewHolders
                                             )

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







        binding.executePendingBindings()
        return view
    }
    fun swap(ID: String)
    {
        var ada = ProfileCommentsAdapter(view!!.context, myViewModel.getUserProfileComments(ID))
        view!!.profile_comment_recyclerView.swapAdapter(ada, true)
    }


    companion object {
        fun newInstance(ID: String): ProfileCommentFragment {
            val bundle : Bundle = Bundle()
            bundle.putString("ID",ID)
            val fragment = ProfileCommentFragment()
            fragment.setArguments(bundle)
            return fragment
        }
    }

    fun clear(ProfileComments : CommentLive){
        ProfileComments.value = null
    }

}