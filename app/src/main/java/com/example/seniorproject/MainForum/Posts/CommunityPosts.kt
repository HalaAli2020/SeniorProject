package com.example.seniorproject.MainForum.Posts

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
//import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.CustomAdapter
import com.example.seniorproject.MainForum.Adapters.CustomViewHolders
import com.example.seniorproject.MainForum.Adapters.PostImageViewHolders
import com.example.seniorproject.R
import com.example.seniorproject.Utils.ButtonClickListener
import com.example.seniorproject.Utils.ProfileButton
import com.example.seniorproject.Utils.SwipeHelper
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.viewModels.CommunityPostViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_community_posts.*
import kotlinx.android.synthetic.main.fragment_profile__post.view.*
import kotlinx.android.synthetic.main.rv_post.view.*
import javax.inject.Inject

class CommunityPosts : AppCompatActivity() {

    private lateinit var adapter: CustomAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: CommunityPostViewModel
    lateinit var obse: Observer<in MutableList<Post>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_posts)

        val className = intent.getStringExtra("ClassName")

        post_list_community_name_TV.text = className

        val factory = InjectorUtils.provideCommunityPostViewModelFacotry()
        myViewModel = ViewModelProviders.of(this, factory).get(CommunityPostViewModel::class.java)


        classes_post_RV.layoutManager = LinearLayoutManager(this)

        adapter = CustomAdapter(this, myViewModel.returnClassPosts(className!!), 1)
        classes_post_RV.adapter = adapter


        refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.blue_theme))
        refreshView.setColorSchemeColors(ContextCompat.getColor(this, R.color.white))

        refreshView.setOnRefreshListener {
            refreshView.isRefreshing = false
            classes_post_RV.adapter = CustomAdapter(this, myViewModel.returnClassPosts(className), 1)
        }

        fun showToast(){
            var toast= Toast.makeText(this@CommunityPosts, "We've received your report.",Toast.LENGTH_SHORT)
            toast.show()
        }


             object : SwipeHelper(applicationContext, classes_post_RV, 200) {
                override fun initButton(
                    viewHolders: RecyclerView.ViewHolder,
                    buffer: MutableList<ProfileButton>
                ) {
                    val userk: String? =
                        adapter.getUserKey(viewHolders)

                    if (FirebaseAuth.getInstance().currentUser?.uid == userk){
                        //val swipe = null
                    }
                    else{
                        buffer.add(
                            ProfileButton(applicationContext, "Block User", 30, 0, Color.parseColor
                                ("#FF0000"), object : ButtonClickListener {
                                override fun onClick(pos: Int) {
                                    //val crnkey: String? =
                                     //   adapter.getCrn(viewHolders)

                                    val userkey: String? =
                                        adapter.getUserKey(viewHolders)

                                    val authkey: String? =
                                        adapter.getAuthor(viewHolders)

                                    //var builder = AlertDialog.Builder(activity!!.baseContext, R.style.AppTheme_AlertDialog)
                                    var builder = AlertDialog.Builder(
                                        this@CommunityPosts,
                                        R.style.AppTheme_AlertDialog
                                    )

                                    //.getStringExtra("Classkey")
                                    //val postkey = intent.getStringExtra("author")
                                    //myViewModel.deletePost(postkey!!, className)
                                    //myViewModel.deletePost()
                                    builder.setTitle("Are you sure?")
                                    builder.setMessage("You won't see posts or comments from this user.")
                                    builder.setPositiveButton("BLOCK"
                                    ) { _: DialogInterface?, _: Int ->
                                        myViewModel.blockUser(userkey!!)
                                        classes_post_RV.findViewHolderForAdapterPosition(pos)!!.itemView.post_title.text="[blocked]"
                                        var count: Int =0
                                        for( i in 0..classes_post_RV.childCount) {
                                            if(classes_post_RV.findViewHolderForAdapterPosition(count)!!.itemView.post_title.text =="[blocked]"){
                                                classes_post_RV.findViewHolderForAdapterPosition(count)!!.itemView.setOnClickListener {
                                                    Toast.makeText(this@CommunityPosts, "You have blocked $authkey and cannot see their posts",
                                                        Toast.LENGTH_SHORT).show()

                                                }
                                            }
                                            //classes_post_RV.findViewHolderForAdapterPosition(count)!!.itemView.post_title.text="[blocked]"
                                            count++
                                            if(count== classes_post_RV.childCount){
                                                break;
                                            }
                                            //classes_post_RV.findViewHolderForAdapterPosition(i)!!.itemView.post_title.text="[blocked]"
                                            //classes_post_RV.getChildViewHolder(classes_post_RV.getChildAt(i)).itemView.post_title.text="[blocked]"
                                        }
                                        var toast = Toast.makeText(
                                            this@CommunityPosts,
                                            "This user has been blocked",
                                            Toast.LENGTH_SHORT
                                        )
                                        toast.show()
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
                            ProfileButton(applicationContext, "Report Post", 30, 0, Color.parseColor
                                ("#D3D3D3"), object : ButtonClickListener {
                                override fun onClick(pos: Int) {
                                    val postkey: String? =
                                        adapter.removeItem(viewHolders)

                                    val userkey: String? =
                                        adapter.getUserKey(viewHolders)

                                    val crnkey: String? =
                                        adapter.getCrn(viewHolders)

                                    val textkey: String? = adapter.getText(viewHolders)

                                    var builder = AlertDialog.Builder(
                                        this@CommunityPosts,
                                        R.style.AppTheme_AlertDialog
                                    )

                                    var listreason = arrayOf(
                                        "This is spam",
                                        "This is abusive or harassing",
                                        "Other issues"
                                    )
                                    builder.setTitle("Report Post")
                                    builder.setSingleChoiceItems(
                                        listreason,
                                        0
                                    ) { _, i ->
                                        //var complaint = listreason[i]
                                    }
                                    builder.setPositiveButton("SUBMIT"
                                    ) { _: DialogInterface?, _: Int ->
                                        var toast = Toast.makeText(
                                            this@CommunityPosts,
                                            "We've received your report.",
                                            Toast.LENGTH_SHORT
                                        )
                                        toast.show()
                                        myViewModel.reportUserPost(
                                            userkey!!,
                                            textkey!!,
                                            crnkey!!,
                                            postkey!!
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

                    }


                 }
            }
        //}


        obse = Observer<MutableList<Post>> {
            Log.d("obser", " blah")

        }

    }
    //myViewModel.listClasses?.observe(this, obse)


}





