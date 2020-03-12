package com.example.seniorproject.MainForum.Fragments

import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
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
import com.example.seniorproject.MainForum.UserProfileActivity

import com.example.seniorproject.R
import com.example.seniorproject.databinding.FragmentProfilePostBinding
import com.example.seniorproject.viewModels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_community_posts.*
import kotlinx.android.synthetic.main.fragment_profile__post.view.*
import kotlinx.android.synthetic.main.fragment_profile__post.view.refreshView
import javax.inject.Inject


class ProfilePostFragment : Fragment() {

    private lateinit var adapter: CustomAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    lateinit var deleteIcon: Drawable

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //this is only place we dont have the ID
        val ID = this.getArguments()?.getString("ID").toString()



        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile__post, container, false)
        DaggerAppComponent.create().inject(this)
        val factory = InjectorUtils.provideProfileViewModelFactory()
        val binding: FragmentProfilePostBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile__post, container, false)
        myViewModel = ViewModelProviders.of(this,factory).get(ProfileViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_profile__post, container, false)


            view.profile_post_recyclerView.adapter =
                CustomAdapter(view.context, myViewModel.getUserProfilePosts(ID),0)

        adapter = CustomAdapter(view.context, myViewModel.getUserProfilePosts(ID),0)
        view.profile_post_recyclerView.adapter= adapter

        view.refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(view.context, R.color.blue_theme))
        view.refreshView.setColorSchemeColors(ContextCompat.getColor(view.context, R.color.white))

        view.refreshView.setOnRefreshListener {
            view.profile_post_recyclerView.adapter =
                CustomAdapter(view.context, myViewModel.getUserProfilePosts(ID),0)
            view.refreshView.isRefreshing = false
        }


        binding.profViewModel = myViewModel
        binding.lifecycleOwner = this

        myViewModel.getUserProfilePosts(ID)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        view.profile_post_recyclerView.layoutManager = linearLayoutManager
        view.profile_post_recyclerView.adapter =
            CustomAdapter(
                view.context,
                myViewModel.getUserProfilePosts(ID),
                0
            )

        deleteIcon = ContextCompat.getDrawable(activity!!.applicationContext, R.drawable.ic_delete_24px)!!


        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onSwiped(viewHolders: RecyclerView.ViewHolder, position: Int) {
                    val postkey: String? =
                        adapter.removeItem(viewHolders as CustomViewHolders, position)

                    val userkey: String? =
                        adapter.getUserKey(viewHolders as CustomViewHolders, position)

                    val crnkey: String? =
                        adapter.getCrn(viewHolders as CustomViewHolders, position)

                    //var builder = AlertDialog.Builder(activity!!.baseContext, R.style.AppTheme_AlertDialog)
                    var builder = AlertDialog.Builder(view.context, R.style.AppTheme_AlertDialog)

                    //.getStringExtra("Classkey")
                    //val postkey = intent.getStringExtra("author")
                    //myViewModel.deletePost(postkey!!, className)
                    //myViewModel.deletePost()
                    builder.setTitle("Are you sure?")
                    builder.setMessage("You cannot restore posts that have been deleted.")
                    builder.setPositiveButton("DELETE",
                        { dialogInterface: DialogInterface?, i: Int ->
                            myViewModel.deletePost(postkey!!, crnkey!!, userkey!!)
                        })
                    builder.setNegativeButton("CANCEL",
                        { dialogInterface: DialogInterface?, i: Int ->
                            builder.setCancelable(true)
                        })

                    val msgdialog: AlertDialog = builder.create()

                    msgdialog.getWindow()!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL)

                    msgdialog.show()

                    //myViewModel.deletePost(postkey!!, className)
                    //classes_post_RV.removeViewAt(viewHolders.adapterPosition)
                    adapter.notifyItemRemoved(viewHolders.adapterPosition)
                    adapter.notifyItemRangeChanged(viewHolders.adapterPosition, adapter.itemCount)
                    view.profile_post_recyclerView.adapter = adapter

                    //adapter.notifyDataSetChanged()
                    //adapter.notifyItemRangeRemoved(viewHolders.adapterPosition, 1)
                    /*adapter.notifyItemRangeChanged(viewHolders.adapterPosition, adapter.itemCount - viewHolders.adapterPosition+1)*/
                    //adapter.notifyDataSetChanged()

                    // adapter.notifyItemRangeRemoved(viewHolders.adapterPosition, 1)
                    // adapter.notifyItemRemoved(viewHolders.adapterPosition)
                    // adapter.notifyItemRangeChanged(viewHolders.adapterPosition, )
                    //  obse.onChanged(pos)

                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView
                    val iconMargin = (itemView.height - deleteIcon.intrinsicHeight)/2

                    if(dX < 0) {
                        swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                        deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, itemView.top + iconMargin,
                            itemView.right - iconMargin, itemView.bottom - iconMargin)
                    }

                    swipeBackground.draw(c)
                    deleteIcon.draw(c)

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }

                /*override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    return super.getMovementFlags(recyclerView, viewHolder)
                }*/

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

        itemTouchHelper.attachToRecyclerView(view.profile_post_recyclerView)

        binding.executePendingBindings()
        return view
    }
    companion object {
        fun newInstance(ID: String): ProfilePostFragment {
            val bundle : Bundle = Bundle()
            bundle.putString("ID",ID)
            val fragment = ProfilePostFragment()
            fragment.setArguments(bundle)
            return fragment
        }
    }


}
