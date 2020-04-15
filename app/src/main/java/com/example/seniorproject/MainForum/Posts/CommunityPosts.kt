package com.example.seniorproject.MainForum.Posts

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seniorproject.Dagger.DaggerAppComponent
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
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

class CommunityPosts : AppCompatActivity() {

    private lateinit var adapter: PostAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var myViewModel: CommunityPostViewModel

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_posts)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val className = intent.getStringExtra("ClassName")

        post_list_community_name_TV.text = className

        //app component function inject is being called here
        DaggerAppComponent.create().inject(this)

        //generic dagger view model factory indirectly initializes factory with multibinding module
        myViewModel = ViewModelProvider(this, factory).get(CommunityPostViewModel::class.java)

        val linearLayoutManager = LinearLayoutManager(this)
        //newest posts appear first
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        classes_post_RV.layoutManager = linearLayoutManager

        //function that calls list created from Kotlin Flow using mutable list callback
        var livedatapostlist = myViewModel.getClassesco(className, object: MutableListCallback{
            override fun onList(list: List<Post>) {

                //initalize adapter with list
                adapter = PostAdapter(this@CommunityPosts, list, 1)
                classes_post_RV.adapter = adapter

                refreshView.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this@CommunityPosts, R.color.blue_theme))
                refreshView.setColorSchemeColors(ContextCompat.getColor(this@CommunityPosts, R.color.white))

                refreshView.setOnRefreshListener {
                    refreshView.isRefreshing = false
                    classes_post_RV.adapter = PostAdapter(this@CommunityPosts, list, 1)
                }

                //swipe functionality for swiping every post in community posts with options to report and block
                object : SwipeHelper(applicationContext, classes_post_RV, 200) {
                    //initButton creates the buffer which holds ProfileButtons that we can add and those buttons will be drawn
                    //accordingly. takes viewHolders, so that button will be set on swipe for each viewHolder in recyclerview
                    override fun initButton(
                        viewHolders: RecyclerView.ViewHolder,
                        buffer: MutableList<ProfileButton>
                    ) {
                        val userk: String? =
                            adapter.getUserKey(viewHolders)

                        if (FirebaseAuth.getInstance().currentUser?.uid == userk){
                            val swipe = null
                            //users cannot report or block themselves
                        }
                        else{
                            //the profile button Block User is added into the buffer that holds the list of buttons.
                            buffer.add(
                                //BlockUser text appears inside button with community posts context and with color Red.
                                ProfileButton(applicationContext, "Block", 45, 0, Color.parseColor
                                    ("#FF0000"), object : ButtonClickListener {
                                    //this onClick listener is triggered when user hits button
                                    override fun onClick(pos: Int) {

                                      //  val userkey: String? =
                                      //      adapter.getUserKey(viewHolders)

                                        val authkey: String? =
                                            adapter.getAuthor(viewHolders)

                                        //initialize builder
                                        val builder = AlertDialog.Builder(
                                            this@CommunityPosts,
                                            R.style.AppTheme_AlertDialog
                                        )
                                        //sets up content of alert dialog
                                        builder.setTitle("Are you sure?")
                                        builder.setMessage("You won't see posts or comments from this user.")
                                        builder.setPositiveButton("BLOCK"
                                        ) { _: DialogInterface?, _: Int ->
                                            myViewModel.blockUser(authkey!!)
                                            val toast = Toast.makeText(
                                                this@CommunityPosts,
                                                "This user has been blocked",
                                                Toast.LENGTH_SHORT
                                            )
                                            finish()
                                            //completes community posts activity
                                            startActivity(getIntent())
                                            //reloads community posts activity with blocked users content gone.
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

                            //the profile button Report Post is added into the buffer that holds the list of buttons.
                            buffer.add(
                                //ReportPost text appears inside button with community posts context and with color Gray.
                                ProfileButton(applicationContext, "Report", 45, 0, Color.parseColor
                                    ("#2b99fd"), object : ButtonClickListener {
                                    //this onClick listener is triggered when user hits button
                                    override fun onClick(pos: Int) {
                                        val postkey: String? =
                                            adapter.removeItem(viewHolders)

                                        val userkey: String? =
                                            adapter.getUserKey(viewHolders)

                                        val crnkey: String? =
                                            adapter.getCrn(viewHolders)

                                        val textkey: String? = adapter.getText(viewHolders)

                                        val builder = AlertDialog.Builder(
                                            this@CommunityPosts,
                                            R.style.AppTheme_AlertDialog
                                        )
                                        //sets up alert dialog with following reasons user could choose from to explain why they are
                                        //reporting
                                        val listreason = arrayOf(
                                            "This is spam",
                                            "This is abusive or harassing",
                                            "Other issues"
                                        )
                                        builder.setTitle("Report Post")
                                        builder.setSingleChoiceItems(
                                            listreason,
                                            0
                                        ) { _, _ ->
                                        }
                                        builder.setPositiveButton("SUBMIT"
                                        ) { _: DialogInterface?, _: Int ->
                                            val toast = Toast.makeText(
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
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}



