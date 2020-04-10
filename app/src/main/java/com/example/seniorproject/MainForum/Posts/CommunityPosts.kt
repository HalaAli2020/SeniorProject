package com.example.seniorproject.MainForum.Posts

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.InjectorUtils
import com.example.seniorproject.MainForum.Adapters.PostAdapter
import com.example.seniorproject.R
import com.example.seniorproject.Utils.ButtonClickListener
import com.example.seniorproject.Utils.MutableListCallback
import com.example.seniorproject.Utils.ProfileButton
import com.example.seniorproject.Utils.SwipeHelper
import com.example.seniorproject.data.models.Post
import com.example.seniorproject.viewModels.CommunityPostViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_community_posts.*
import kotlinx.android.synthetic.main.rv_post.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

class CommunityPosts : AppCompatActivity() {

    private lateinit var adapter: PostAdapter
    var mutablepostlist: ArrayList<Post> = arrayListOf()

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: CommunityPostViewModel
    private lateinit var obse: Observer<in MutableList<Post>>


    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_posts)

        val className = intent.getStringExtra("ClassName")

        post_list_community_name_TV.text = className

        val factory = InjectorUtils.provideCommunityPostViewModelFacotry()
        myViewModel = ViewModelProviders.of(this, factory).get(CommunityPostViewModel::class.java)


        classes_post_RV.layoutManager = LinearLayoutManager(this)


        var livedatapostlist = myViewModel.getClassesco(className, object: MutableListCallback{
            override fun onList(list: List<Post>) {
                Log.d("soupview", "start")

                adapter = PostAdapter(this@CommunityPosts, list, 1)
                classes_post_RV.adapter = adapter

                refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this@CommunityPosts, R.color.blue_theme))
                refreshView.setColorSchemeColors(ContextCompat.getColor(this@CommunityPosts, R.color.white))

                refreshView.setOnRefreshListener {
                    refreshView.isRefreshing = false
                    classes_post_RV.adapter = PostAdapter(this@CommunityPosts, list, 1)
                }


                object : SwipeHelper(applicationContext, classes_post_RV, 200) {
                    override fun initButton(
                        viewHolders: RecyclerView.ViewHolder,
                        buffer: MutableList<ProfileButton>
                    ) {
                        val userk: String? =
                            adapter.getUserKey(viewHolders)

                        if (FirebaseAuth.getInstance().currentUser?.uid == userk){
                            val swipe = null
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


                                        var builder = AlertDialog.Builder(
                                            this@CommunityPosts,
                                            R.style.AppTheme_AlertDialog
                                        )

                                        builder.setTitle("Are you sure?")
                                        builder.setMessage("You won't see posts or comments from this user.")
                                        builder.setPositiveButton("BLOCK"
                                        ) { _: DialogInterface?, _: Int ->
                                            myViewModel.blockUser(userkey!!)
                                            classes_post_RV.findViewHolderForAdapterPosition(pos)!!.itemView.post_title.text="[blocked]"
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


                obse = Observer<MutableList<Post>> {
                    Log.d("obser", " blah")

                }
            }
        })

    }

}



