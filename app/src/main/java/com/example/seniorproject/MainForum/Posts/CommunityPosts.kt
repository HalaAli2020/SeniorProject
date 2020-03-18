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
import com.example.seniorproject.R
import com.example.seniorproject.Utils.ButtonClickListener
import com.example.seniorproject.Utils.ProfileButton
import com.example.seniorproject.Utils.SwipeHelper
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.viewModels.CommunityPostViewModel
import kotlinx.android.synthetic.main.activity_community_posts.*
import kotlinx.android.synthetic.main.fragment_profile__post.view.*
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
            classes_post_RV.adapter = CustomAdapter(this, myViewModel.returnClassPosts(className!!), 1)
        }

        fun showToast(){
            var toast= Toast.makeText(this@CommunityPosts, "We've received your report.",Toast.LENGTH_SHORT)
            toast.show()
        }

        val swipe = object : SwipeHelper(applicationContext, classes_post_RV, 200) {
            override fun initButton(
                viewHolders: RecyclerView.ViewHolder,
                buffer: MutableList<ProfileButton>
            ) {
                buffer.add(
                    ProfileButton(applicationContext,"Report Post", 30,0,Color.parseColor
                        ("#FF0000"), object: ButtonClickListener {
                        override fun onClick(pos: Int) {
                            val postkey: String? =
                                adapter.removeItem(viewHolders as CustomViewHolders, pos)

                            val userkey: String? =
                                adapter.getUserKey(viewHolders as CustomViewHolders, pos)

                            val crnkey: String? =
                                adapter.getCrn(viewHolders as CustomViewHolders, pos)

                            val textkey: String? = adapter.getText(viewHolders, pos)

                            var builder = AlertDialog.Builder(this@CommunityPosts, R.style.AppTheme_AlertDialog)

                            var listreason= arrayOf("This is spam", "This is abusive or harassing","Other issues")
                            //.getStringExtra("Classkey")
                            //val postkey = intent.getStringExtra("author")
                            //myViewModel.deletePost(postkey!!, className)
                            //myViewModel.deletePost()
                            builder.setTitle("Report Post")
                            builder.setSingleChoiceItems(listreason, 0){ dialogInterface, i ->
                                var complaint= listreason[i]
                            }
                            builder.setPositiveButton("SUBMIT",
                                { dialogInterface: DialogInterface?, i: Int ->
                                    /*if(listreason[i] ==null){
                                        var toast= Toast.makeText(this@CommunityPosts, "Please"+pos,Toast.LENGTH_SHORT)
                                        toast.show()
                                    }*/
                                    var toast= Toast.makeText(this@CommunityPosts, "We've received your report.",Toast.LENGTH_SHORT)
                                    toast.show()
                                    myViewModel.reportUserPost(userkey!!, textkey!!, crnkey!!, postkey!!)

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
                    ProfileButton(applicationContext,"Block User", 30,0, Color.parseColor
                        ("#D3D3D3"), object: ButtonClickListener {
                        override fun onClick(pos: Int) {
                            val postkey: String? =
                                adapter.removeItem(viewHolders as CustomViewHolders, pos)

                            val userkey: String? =
                                adapter.getUserKey(viewHolders as CustomViewHolders, pos)

                            val crnkey: String? =
                                adapter.getCrn(viewHolders as CustomViewHolders, pos)

                            //var builder = AlertDialog.Builder(activity!!.baseContext, R.style.AppTheme_AlertDialog)
                            var builder = AlertDialog.Builder(this@CommunityPosts, R.style.AppTheme_AlertDialog)

                            //.getStringExtra("Classkey")
                            //val postkey = intent.getStringExtra("author")
                            //myViewModel.deletePost(postkey!!, className)
                            //myViewModel.deletePost()
                            builder.setTitle("Are you sure?")
                            builder.setMessage("You won't see posts or comments from this user.")
                            builder.setPositiveButton("BLOCK",
                                { dialogInterface: DialogInterface?, i: Int ->
                                    //myViewModel.blockPost(userkey!!, crnkey!!, postkey!!)
                                    var toast= Toast.makeText(this@CommunityPosts, "This user has been blocked",Toast.LENGTH_SHORT)
                                    toast.show()
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

            }
        }


        obse = Observer<MutableList<Post>> {
            Log.d("obser", " blah")

        }

    }
    //myViewModel.listClasses?.observe(this, obse)


}





